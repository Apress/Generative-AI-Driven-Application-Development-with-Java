package com.example.demo.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class OrchestratorWorkersWorkflow {
    private final ChatClient chatClient;
    private final WorkerResponse workerResponse;

    public OrchestratorWorkersWorkflow(ChatClient chatClient) {
        this.chatClient = chatClient;
        this.workerResponse = new WorkerResponse();
    }

    public WorkerResponse process(String s) throws JsonProcessingException {
        String response = Objects.requireNonNull(this.chatClient.prompt("""
                                There are two types of documentation for a REST API endpoint: technical and user-friendly.
                                Technical documentation is intended for developers and includes details about the API's functionality, parameters, and response formats.
                                User-friendly documentation is intended for end-users and includes information about how to use the API, examples, and best practices.
                                There are two types of workers: technical and user-friendly. Either one or Both the workers can be used simultaneously to generate documentation.
                                The technical worker generates technical documentation, while the user-friendly worker generates user-friendly documentation.
                                The technical worker should be used when the user requests technical documentation, and the user-friendly worker should be used when the user requests user-friendly documentation.
                                If the user requests both, both the technical and user-friendly workers should be used.
                                The user will provide a prompt that specifies the type of documentation they want.
                                Generate only a JSON string that contains the following fields without any formatting or extra text or any surrounding quotes or json text or language description:
                                - "technical": a boolean value that indicates whether the technical documentation should be generated
                                - "userFriendly": a boolean value that indicates whether the user-friendly documentation should be generated
                                The user will provide a prompt that specifies the type of documentation they want:
                                """)
                        .user(s)
                        .call()
                        .content())
                .replaceAll("^```json\n|\n```$", "");
        Map<String, Boolean> responseMap = new ObjectMapper().readValue(response, new TypeReference<Map<String, Boolean>>() {});

        CompletableFuture<Void> technicalFuture = CompletableFuture.runAsync(() -> {
            if (responseMap.getOrDefault("technical", false)) {
                technicalWorker(chatClient);
            }
        });

        CompletableFuture<Void> userFriendlyFuture = CompletableFuture.runAsync(() -> {
            if (responseMap.getOrDefault("userFriendly", false)) {
                userFriendlyWorker(chatClient);
            }
        });

        CompletableFuture.allOf(technicalFuture, userFriendlyFuture).join();

        String analysis = this.chatClient.prompt("Analyze the response and provide a summary of the documentation generated")
                .user(s)
                .call()
                .content();
        workerResponse.setAnalysis(analysis);

        return workerResponse;
    }

    public String technicalWorker(ChatClient chatClient) {
        String response = this.chatClient.prompt("Generate technical documentation for a REST API endpoint")
                .call()
                .content();
        workerResponse.setWorkerResponses(response);
        return response;
    }

    public String userFriendlyWorker(ChatClient chatClient) {
        String response = this.chatClient.prompt("Generate user-friendly documentation for a REST API endpoint")
                .call()
                .content();
        workerResponse.setWorkerResponses(response);
        return response;
    }
}
