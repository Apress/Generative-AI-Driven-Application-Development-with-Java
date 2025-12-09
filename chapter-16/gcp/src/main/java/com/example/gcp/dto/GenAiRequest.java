package com.example.gcp.dto;

/** Simple request body for /api/genai POST. */
public record GenAiRequest(String prompt, String modelId) { }

