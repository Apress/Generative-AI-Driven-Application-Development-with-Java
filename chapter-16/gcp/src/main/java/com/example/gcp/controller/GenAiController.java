package com.example.gcp.controller;

import com.example.gcp.dto.GenAiRequest;
import com.example.gcp.dto.GenAiResponse;
import com.example.gcp.service.GenAiService;
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