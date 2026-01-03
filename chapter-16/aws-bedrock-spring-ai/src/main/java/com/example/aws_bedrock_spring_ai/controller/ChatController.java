package com.example.aws_bedrock_spring_ai.controller;

import com.example.aws_bedrock_spring_ai.dto.ChatRequestDto;
import com.example.aws_bedrock_spring_ai.dto.ChatResponseDto;
import com.example.aws_bedrock_spring_ai.service.ChatService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponseDto> chat(@RequestBody @Valid ChatRequestDto body) {
        String reply = chatService.chat(body.getPrompt());
        return ResponseEntity.ok(new ChatResponseDto(reply));
    }
}
