package com.example.gcp.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

@Service
public class GenAiService {

    private final Client client;

    public GenAiService() {
        // Default ctor picks up env vars
        this.client = new Client();
    }

    /**
     * Calls Gemini (or Vertex) and returns the text response.
     *
     * @param prompt  the user prompt
     * @param modelId optional model (defaults to "gemini-2.0-flash-001")
     */
    public String generate(String prompt, String modelId) {
        String m = (modelId == null || modelId.isBlank())
                ? "gemini-2.0-flash-001"
                : modelId;

        GenerateContentResponse res = client.models.generateContent(m, prompt, null);
        return res.text();          // convenient helper
    }
}
