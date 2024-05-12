package com.project.search.controller;

import com.project.search.model.SearchResult;
import com.project.search.model.VectorResponse;
import com.project.search.model.VectorSearchRequest;
import com.project.search.model.CombineSearchRequest;
import com.project.search.service.VectorSearchService;
import com.project.search.service.VectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class VectorSearchController {

    @Autowired
    private VectorSearchService vectorSearchService;

    @Autowired
    private VectorService vectorService;

    @PostMapping("/vector_search")
    public ResponseEntity<List<SearchResult>> vectorSearchImages(@RequestBody VectorSearchRequest request) {
        try {
            VectorResponse vectorResponse = vectorService.vectorizeText(request.getQuery());
            List<Double> vectorList = vectorResponse.getVector();
            float[] queryVector = new float[vectorList.size()];
            for (int i = 0; i < vectorList.size(); i++) {
                queryVector[i] = vectorList.get(i).floatValue();
            }

            List<SearchResult> searchResults = vectorSearchService.vectorSearch(queryVector, request.getImageWeight(), request.getNameWeight(), 
            request.getTextWeight(), request.getAmount());
            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/combine_search")
    public ResponseEntity<List<SearchResult>> combineSearchImages(@RequestBody CombineSearchRequest request) {
        try {
            VectorResponse vectorResponse = vectorService.vectorizeText(request.getQuery());
            List<Double> vectorList = vectorResponse.getVector();
            float[] queryVector = new float[vectorList.size()];
            for (int i = 0; i < vectorList.size(); i++) {
                queryVector[i] = vectorList.get(i).floatValue();
            }

            List<SearchResult> searchResults = vectorSearchService.combineSearch(request.getQuery(), queryVector, request.getTextBoost(), 
            request.getVectorBoost(), request.getAmount());
            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
