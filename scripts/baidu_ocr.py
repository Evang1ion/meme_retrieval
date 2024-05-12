from pymongo import MongoClient
from aip import AipOcr
import requests

#Configure Baidu OCR
APP_ID = '53500692'
API_KEY = 'XR8ajtrticAMztoV1UxkdNrC'
SECRET_KEY = 'UVRzgM1pyMn9ccHXNZ7DADEmdSGDdVjR'
client_ocr = AipOcr(APP_ID, API_KEY, SECRET_KEY)

client_mongo = MongoClient('mongodb://localhost:27017/')
db = client_mongo['search']
collection = db['memeCH']

#Find the record index
start_index = collection.find_one({'name': '国庆节00023-国庆节快到了可爱的群主已经穿成这样了丑的群主只想蒙混过关.jpg'})

if start_index:
    records = collection.find({'_id': {'$gt': start_index['_id']}}).limit(200)
else:
    print("Start record '国庆节00023-国庆节快到了可爱的群主已经穿成这样了丑的群主只想蒙混过关.jpg.")
    records = []

for record in records:
    image_url = record['url']
    
    #Skip GIF images
    if image_url.lower().endswith('.gif'):
        print("Skipping GIF image:", image_url)
        continue
    
    try:
        response = requests.get(image_url)
        image_content = response.content
    except Exception as e:
        print(f"Failed to download image: {e}")
        continue

    #OCR options
    options = {
        "language_type": "CHN_ENG",
        "detect_direction": "true",
        "detect_language": "true",
        "probability": "true"
    }
    result = client_ocr.basicGeneral(image_content, options)

    recognized_text = '\n'.join([item['words'] for item in result.get('words_result', [])])

    #Print recognized text
    for item in result.get('words_result', []):
        print(item['words'])
    
    #Update the database record
    collection.update_one({'_id': record['_id']}, {'$set': {'text': recognized_text}})

print("Text recognition completed and updated to the database.")
