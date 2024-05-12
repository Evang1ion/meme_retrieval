from flask import Flask, request, jsonify
import torch
import cn_clip.clip as clip
from cn_clip.clip import load_from_name

app = Flask(__name__)

# initialize Chinese CLIP model
device = "cuda" if torch.cuda.is_available() else "cpu"
model, preprocess = load_from_name("ViT-B-16", device=device)

@app.route('/vectorize', methods=['POST'])
def vectorize_text():
    data = request.json
    text = data.get("text")
    if not text:
        return jsonify({"error": "No text provided"}), 400

    # vectorize the query text
    text_input = clip.tokenize([text]).to(device)
    with torch.no_grad():
        text_features = model.encode_text(text_input)
        text_features /= text_features.norm(dim=-1, keepdim=True)
        text_vector = text_features.cpu().numpy().flatten().tolist()

    return jsonify({"vector": text_vector})

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
