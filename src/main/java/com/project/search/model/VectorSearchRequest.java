package com.project.search.model;

public class VectorSearchRequest {
    private String query;
    private float imageWeight;
    private float nameWeight;
    private float textWeight;
    private int amount;

    // Getters
    public String getQuery() {
        return query;
    }

    public float getImageWeight() {
        return imageWeight;
    }

    public float getNameWeight() {
        return nameWeight;
    }

    public float getTextWeight() {
        return textWeight;
    }

    public int getAmount() {
        return amount;
    }

    // Setters
    public void setQuery(String query) {
        this.query = query;
    }

    public void setImageWeight(float imageWeight) {
        this.imageWeight = imageWeight;
    }

    public void setNameWeight(float nameWeight) {
        this.nameWeight = nameWeight;
    }

    public void setTextWeight(float textWeight) {
        this.textWeight = textWeight;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
