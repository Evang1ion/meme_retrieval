import io
import requests
from PIL import Image
import torch
import numpy as np
import cn_clip.clip as clip
from pymongo import MongoClient
from elasticsearch import Elasticsearch
import urllib3

def process_and_index_document(doc, es, model, preprocess, device, index_name):
    image_url = doc['url']
    text = doc.get('text', '')
    name = doc['name']

    try:
        response = requests.get(image_url, verify=False)
        image_data = Image.open(io.BytesIO(response.content)).convert("RGB")
        image_preprocessed = preprocess(image_data).unsqueeze(0).to(device)

        with torch.no_grad():
            image_features = model.encode_image(image_preprocessed)
            image_features /= image_features.norm(dim=-1, keepdim=True)
            
        text_tokens = clip.tokenize([text, name]).to(device)
        with torch.no_grad():
            text_features, name_features = model.encode_text(text_tokens)
            text_features /= text_features.norm(dim=-1, keepdim=True)
            name_features /= name_features.norm(dim=-1, keepdim=True)

        doc_to_index = {
            "image_url": image_url,
            "name": name,
            "text": text,
            "image_vector": image_features.cpu().numpy().flatten().tolist(),
            "name_vector": name_features.cpu().numpy().flatten().tolist(),
            "text_vector": text_features.cpu().numpy().flatten().tolist(),
        }

        es.index(index=index_name, body=doc_to_index)
        return True
    except Exception as e:
        print(f"Error processing document {name}: {e}", flush=True)
        return False

urllib3.disable_warnings()

es = Elasticsearch(
    ["http://10.122.231.38:9200/"],
    basic_auth=('elastic', 'yNzpqIvkODsyqo7IKk5a'),
)

client = MongoClient('mongodb://localhost:27017/')
db = client['search']
collection = db['memeCH']

# 初始化CLIP模型
device = "cuda" if torch.cuda.is_available() else "cpu"
model, preprocess = clip.load_from_name("ViT-B-16", device=device)
model.eval()

# 限制处理的文档数量和索引名称
max_docs = 2600
processed_docs = 0
failed_docs = []
index_name = "meme_vector"

# 从上次的位置继续
last_processed_name = 'WhiteVillain00475.jpg'
if last_processed_name:
    last_processed_doc = collection.find_one({'name': last_processed_name})
    start_position = {'_id': {'$gt': last_processed_doc['_id']}}
else:
    start_position = {}

for doc in collection.find(start_position):
    if processed_docs >= max_docs:
        print(f"Reached max document limit of {max_docs}.", flush=True)
        break

    success = process_and_index_document(doc, es, model, preprocess, device, index_name)
    if success:
        processed_docs += 1
        print(f"Processed {processed_docs}/{max_docs} documents. Last indexed document: {doc['name']}", flush=True)
    else:
        failed_docs.append(doc['name'])

# 重试失败的文档
for name in failed_docs:
    doc = collection.find_one({'name': name})
    if not doc:
        print(f"Failed document {name} not found in MongoDB.", flush=True)
        continue
    
    success = process_and_index_document(doc, es, model, preprocess, device, index_name)
    if not success:
        print(f"Failed to process document {name} again.", flush=True)

if failed_docs:
    print(f"Failed documents after retry: {failed_docs}", flush=True)
else:
    print("All documents processed successfully.", flush=True)
