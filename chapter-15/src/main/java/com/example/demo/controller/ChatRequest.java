package com.example.demo.controller;

public class ChatRequest {
    private String userText;

    public ChatRequest() {}

    public ChatRequest(String userText) {
        this.userText = userText;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }
}
