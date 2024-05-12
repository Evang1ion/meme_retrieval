import requests
from pymongo import MongoClient

url = "https://raw.githubusercontent.com/zhaoolee/ChineseBQB/master/chinesebqb_github.json"
response = requests.get(url)
data = response.json()

client = MongoClient('mongodb://localhost:27017/')
db = client['search']
collection = db['memeCH']

#choose the start index's name
start_index = next((index for (index, d) in enumerate(data['data']) if d['name'] == "同福客栈00029-啊啊啊啊啊给你.JPG"), None)

if start_index is not None:
    filtered_items = [item for item in data['data'][start_index + 1:] if not item['url'].endswith('.gif')]

    #choose the amount of data that you want to insert
    to_insert = filtered_items[:3500]
    result = collection.insert_many(to_insert)

    print(f"Inserted {len(result.inserted_ids)} records.")
else:
    print("Starting item not found.")
