package com.example.travelx;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private ServiceWithPersistentMemory chatService;

    public ChatController(ServiceWithPersistentMemory chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public Aircraft chat(@RequestBody ChatRequest chatRequest) {
        return this.chatService.chat(chatRequest.getMessage());
    }
}