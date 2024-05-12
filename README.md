# meme_retrieval
A multimodel internet meme retrieval framework.
## Goal:
The goal is to create a system capable of quickly and accurately identifying memes by analyzing both visual content and textual information. 
## System design:
Throughout the development process, the project opted for an open-source Chinese memedataset from GitHub and the Baidu OCR tool known for its high precision in Chinese andEnglish text recognition. In terms of text and image encoding, the project utilized CHINESECLIP to encode images and dual texts, storing the encoded image and text vectors in an Elasticsearch vector database deployed via Docker. Interaction with Elasticsearch through Kibana's Dev Tools facilitated efficient iteration and debugging throughout the development process. 
<center>![image](https://github.com/Evang1ion/meme_retrieval/assets/104999640/3897e0e8-1975-4fe4-beb1-bfb69932f9d1)</center>
## Environment
### Data processing
Python: 3.7 or above<br>
Flask: 2.0.1<br>
PyTorch: 1.9.0<br>
cn_clip: 1.0.0<br>
BaiduOCR(check baidu_ocr.py)<br>
MongoDB(Only used for data transmission, can be changed)<br>
Elasticsearch: 8.12.1(with IK analyzer, for vector storage and retrieval, its deployed on the cloud server 10.122.231.38 with vectors stored)<br>
Kibana: 8.12.1
### Retrieval system
Java: 17<br>
Spring Boot: 3.2.2<br>
Maven 3.9.5
## Program Structure
Explaination for some imprtant program files.
### scripts
baidu_ocr.py *For recongnizing meme text.*<br>
set_vector_mapping.py *Setting the es mapping.*<br>
**text_image_encoder.py** *Encoding the text and image of the memes into embeddings and storing them in the Elasticsearch vector database.*<br>
**clip_vectorize.py** *An important microservice for vectorizing queries into CLIP embeddings and comparing them with those in the vector database.*
### src
#### config
*cors config and es config*
#### controller
**VectorSearchController** *The main controller for the two retrieval functions. The other text controller is for earlier testing purposes and can be ignored.*
#### model
*Some models are for testing too.*
#### service
**VectorService.java** *For the microservice clip_vectorize.py, responsible for vectorizing queries into CLIP embeddings.*<br>
**VectorSearchService.java** *The two retreive method algorithm, one is vector search and one is fusion search.*
#### front
**Ignore the HTML files in the front folder. The HTML files in src\main\resources\static are the actual frontend programs.**
## Installation Instructions: 
1. Create a JAR file using Maven.<br>
2. Start the microservice clip_vectorize.py.<br>
3. Run the JAR file.<br>
*The final version of the JAR file and microservice has been uploaded to the shijiaqi folder on the cloud server 10.122.231.38. You can access the microservice and run the JAR file by entering the conda memesjq environment. Visit http://10.122.231.38:8080/vector_search.html to access the application. <br>ps:Need to check the conda python path, sometimes the path is not right.*<br>

Please follow these instructions to install and run the meme_retrieval framework.
