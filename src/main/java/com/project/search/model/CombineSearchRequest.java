package com.project.search.model;

public class CombineSearchRequest {
    private String query;
    private float textBoost;
    private float vectorBoost;
    private int amount;

    // Getters
    public String getQuery() {
        return query;
    }

    public float getTextBoost() {
        return textBoost;
    }

    public float getVectorBoost() {
        return vectorBoost;
    }

    public int getAmount() {
        return amount;
    }

    // Setters
    public void setQuery(String query) {
        this.query = query;
    }

    public void setTextBoost(float textBoost) {
        this.textBoost = textBoost;
    }

    public void setVectorBoost(float vectorBoost) {
        this.vectorBoost = vectorBoost;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
