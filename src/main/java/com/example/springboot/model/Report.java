package com.example.springboot.model;

public class Report {
    private String type;
    private String complexity;
    public int extractableCharacterCount;
    public Report(String type, String complexity, int extractableCharacterCount) {
        this.type = type;
        this.complexity = complexity;
        this.extractableCharacterCount = extractableCharacterCount;
    }
    public String getType(){
        return this.type;
    }
    public String getComplexity(){
        return this.complexity;
    }
    public int getExtractableCharacterCount() { return this.extractableCharacterCount; }
}