package com.project.search.controller;

import com.project.search.model.TextRequest;
import com.project.search.model.VectorResponse;
import com.project.search.service.VectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TextVectorController {

    @Autowired
    private VectorService vectorService;

    @PostMapping("/vectorize")
    public ResponseEntity<VectorResponse> vectorizeText(@RequestBody TextRequest textRequest) {
        VectorResponse response = vectorService.vectorizeText(textRequest.getText());
        return ResponseEntity.ok(response);
    }
}
