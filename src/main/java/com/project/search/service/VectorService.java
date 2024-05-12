package com.project.search.service;

import com.project.search.model.TextRequest;
import com.project.search.model.VectorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VectorService {

    @Autowired
    private RestTemplate restTemplate;

    public VectorResponse vectorizeText(String text) {
        TextRequest request = new TextRequest();
        request.setText(text);
        
        VectorResponse response = restTemplate.postForObject("http://localhost:5000/vectorize", 
        request, VectorResponse.class);
        
        return response;
    }
}
