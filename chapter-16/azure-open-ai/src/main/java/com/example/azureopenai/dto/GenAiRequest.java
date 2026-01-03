package com.example.azureopenai.dto;

/** Simple request body for /api/genai POST. */
public record GenAiRequest(String prompt, String modelId) { }
