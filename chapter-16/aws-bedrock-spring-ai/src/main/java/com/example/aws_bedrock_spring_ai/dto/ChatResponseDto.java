package com.example.aws_bedrock_spring_ai.dto;

public class ChatResponseDto {

    private final String content;

    public ChatResponseDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
