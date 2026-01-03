package com.example.azureopenai.controller;

import com.example.azureopenai.dto.GenAiRequest;
import com.example.azureopenai.dto.GenAiResponse;
import com.example.azureopenai.service.GenAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/genai")
public class GenAiController {

    private final GenAiService genAiService;

    public GenAiController(GenAiService genAiService) {
        this.genAiService = genAiService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<GenAiResponse> generatePost(@RequestBody GenAiRequest req) {
        String answer = genAiService.generate(req.prompt(), req.modelId());
        return ResponseEntity.ok(new GenAiResponse(answer));
    }
}