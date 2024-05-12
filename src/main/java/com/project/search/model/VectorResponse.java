package com.project.search.model;

import java.util.List;

public class VectorResponse {
    private List<Double> vector;

    public VectorResponse() {}

    public List<Double> getVector() {
        return vector;
    }

    public void setVector(List<Double> vector) {
        this.vector = vector;
    }
}
