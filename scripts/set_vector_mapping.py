from elasticsearch import Elasticsearch

es = Elasticsearch(
    ["http://10.122.231.38:9200/"],
    basic_auth=('elastic', 'yNzpqIvkODsyqo7IKk5a'),
)

index_name = "meme_vector"

settings_and_mapping = {
    "settings": {
        "analysis": {
            "analyzer": {
                "ik_max_word": {
                    "type": "custom",
                    "tokenizer": "ik_max_word"
                },
                "ik_smart": {
                    "type": "custom",
                    "tokenizer": "ik_smart"
                }
            }
        }
    },
    "mappings": {
        "properties": {
            "image_url": {"type": "keyword"},
            "name": {"type": "text", "analyzer": "ik_max_word"},
            "text": {"type": "text", "analyzer": "ik_max_word"},
            "image_vector": {"type": "dense_vector", "dims": 512},
            "name_vector": {"type": "dense_vector", "dims": 512},
            "text_vector": {"type": "dense_vector", "dims": 512}
        }
    }
}

# 使用 index 方法创建或更新索引的设置和映射
es.indices.create(index=index_name, body=settings_and_mapping, ignore=400)
