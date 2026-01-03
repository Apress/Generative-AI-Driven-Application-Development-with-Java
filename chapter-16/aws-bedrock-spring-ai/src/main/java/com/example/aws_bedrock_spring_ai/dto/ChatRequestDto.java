package com.example.aws_bedrock_spring_ai.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatRequestDto {

    @NotBlank
    private String prompt;

    public String getPrompt()            { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
}
