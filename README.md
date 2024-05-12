# meme_retrieval
A multimodel internet meme retrieval framework.
## Goal:
The goal is to create a system capable of quickly and accurately identifying memes by analyzing both visual content and textual information. 
## System design:
Throughout the development process, the project opted for an open-source Chinese memedataset from GitHub and the Baidu OCR tool known for its high precision in Chinese andEnglish text recognition. In terms of text and image encoding, the project utilized CHINESECLIP to encode images and dual texts, storing the encoded image and text vectors in an Elasticsearch vector database deployed via Docker. Interaction with Elasticsearch through Kibana's Dev Tools facilitated efficient iteration and debugging throughout the development process. 
![image](https://github.com/Evang1ion/meme_retrieval/assets/104999640/3897e0e8-1975-4fe4-beb1-bfb69932f9d1)
## Environment
### Data processing
Python: 3.7 or above<br>
Flask: 2.0.1<br>
PyTorch: 1.9.0<br>
cn_clip: 1.0.0<br>
BaiduOCR(check baidu_ocr.py)<br>
MongoDB(Only used for data transmission, can be changed)<br>
Elasticsearch: 8.12.1(with IK analyzer, for vector storage and retrieval, its deployed on the cloud server 10.122.231.38 with vectors stored)<br>
Kibana: 8.12.1<br>
### Retrieval system
Java: 17<br>
Spring Boot: 3.2.2<br>
Maven 3.9.5<br>
## Program Structure
Explaination for some imprtant program files.
### scripts
baidu_ocr.py *For recongnizing meme text.*
set_vector_mapping.py *Setting the es mapping.*
**text_image_encoder.py** *Encoding the text and image of the memes into embeddings then store it to the es vector database.*
**clip_vectorize.py** *Important microservice, for vectorizing query to CLIP embeddings, then compare the embedding with those in the vector database.*
### src
#### config
*cors config and es config*
#### controller
**VectorSearchController** *Main controller for the two retrieve function, the other text controller is just for testing earlier, ignore it.)*
#### model
*Some models are for testing too.*
#### service
**
