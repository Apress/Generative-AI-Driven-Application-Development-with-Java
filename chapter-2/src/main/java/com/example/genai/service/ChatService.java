package com.example.genai.service;

import com.example.genai.model.ChatCompletionRequest;
import com.example.genai.model.ChatCompletionResponse;
import com.example.genai.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Arrays;

@Service
public class ChatService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public ChatCompletionResponse getChatResponse(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();

        // Build the request body
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel("gpt-4o-mini");
        Message systemMessage = new Message();
        systemMessage.setRole("system");
        systemMessage.setContent("You are a helpful assistant.");

        Message userChatMessage = new Message();
        userChatMessage.setRole("user");
        userChatMessage.setContent(userMessage);

        request.setMessages(Arrays.asList(systemMessage, userChatMessage));

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        HttpEntity<ChatCompletionRequest> entity = new HttpEntity<>(request, headers);

        // Make the POST request
        ResponseEntity<ChatCompletionResponse> response = restTemplate.postForEntity(
                OPENAI_API_URL,
                entity,
                ChatCompletionResponse.class
        );

        return response.getBody();
    }
}
