package com.example.aws_bedrock_spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatModel chatModel;

    public ChatService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String chat(String prompt) {
        return ChatClient
                .create(this.chatModel)
                .prompt(prompt)
                .call()
                .content();
    }
}