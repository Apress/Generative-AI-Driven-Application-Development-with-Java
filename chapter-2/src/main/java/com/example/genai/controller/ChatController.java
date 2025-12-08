package com.example.genai.controller;

import com.example.genai.model.ChatCompletionResponse;
import com.example.genai.model.ChatRequest;
import com.example.genai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public Map<String, String> chat(@RequestBody ChatRequest chatRequest) {
        ChatCompletionResponse response = chatService.getChatResponse(chatRequest.getUserMessage());

        // Extracting the content from the response
        String content = response.getChoices().get(0).getMessage().getContent();

        // Creating a response map
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("content", content);

        return responseMap;
    }
}
