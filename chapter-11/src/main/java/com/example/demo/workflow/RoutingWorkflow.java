package com.example.demo.workflow;

import org.springframework.ai.chat.client.ChatClient;

import java.util.Map;
import java.util.Objects;

public class RoutingWorkflow {
    public RoutingWorkflow(ChatClient chatClient) {
    }

    public String route(String input, Map<String, String> routes, ChatClient chatClient) {
        // Get all keys from the routes map
        StringBuilder keys = new StringBuilder();

        for (String key : routes.keySet()) {
            keys.append(key).append(", ");
        }

        // Remove the last comma and space
        if (!keys.isEmpty()) {
            keys.setLength(keys.length() - 2);
        }

        String routeKey = Objects.requireNonNull(chatClient.prompt("Determine the route for the input: " + input + ". Available routes: " + keys.toString() + ". Just respond with the route name.")
                        .call()
                        .content())
                .trim()
                .toLowerCase();
        String routeMessage = routes.get(routeKey);
        return chatClient.prompt(routeMessage).call().content();
    }
}
