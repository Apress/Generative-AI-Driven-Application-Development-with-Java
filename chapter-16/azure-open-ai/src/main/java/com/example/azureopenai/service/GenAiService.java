package com.example.azureopenai.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenAiService {

    private final OpenAIClient client;
    private final String defaultDeployment;

    public GenAiService(
            @Value("${azure.openai.endpoint}")   String endpoint,
            @Value("${azure.openai.api-key}")    String apiKey,
            @Value("${azure.openai.deployment}") String defaultDeployment) {

        this.defaultDeployment = defaultDeployment;

        this.client = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(apiKey))
                .buildClient();
    }

    public String generate(String prompt, String modelId) {
        String deployment = (modelId == null || modelId.isBlank())
                ? defaultDeployment
                : modelId;

        List<ChatRequestMessage> messages = List.of(new ChatRequestUserMessage(prompt));

        ChatCompletionsOptions opts = new ChatCompletionsOptions(messages)
                .setMaxTokens(1_024)   // pick a sensible number; adjust to your policy/quota
                .setTemperature(0.7)
                .setTopP(0.95);

        ChatCompletions completions = client.getChatCompletions(deployment, opts);

        /* In production, you should add defensive checks for:
         *   - completions == null
         *   - completions.getChoices().isEmpty()
         *   - multiple choices (pick best by log‑prob, etc.)
         */
        return completions.getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}
