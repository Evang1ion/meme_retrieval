package com.project.search.model;

public class SearchResult {
    private String imageUrl;
    private double similarityScore;

    public SearchResult() {
    }

    public SearchResult(String imageUrl, double similarityScore) {
        this.imageUrl = imageUrl;
        this.similarityScore = similarityScore;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }
}
