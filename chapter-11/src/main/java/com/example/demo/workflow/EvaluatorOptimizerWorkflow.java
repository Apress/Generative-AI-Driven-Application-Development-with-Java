package com.example.demo.workflow;

import org.springframework.ai.chat.client.ChatClient;

import java.util.Objects;

public class EvaluatorOptimizerWorkflow {
    private final ChatClient chatClient;
    private final RefinedResponse refinedResponse;

    public EvaluatorOptimizerWorkflow(ChatClient chatClient) {
        this.chatClient = chatClient;
        this.refinedResponse = new RefinedResponse();
    }

    public RefinedResponse loop(String task) {


        String chainOfThought = "NEEDS_IMPROVEMENT";
        String solution = "";

        while (!chainOfThought.equals("PASS")) {
            solution = Objects.requireNonNull(chatClient.prompt(task)
                            .call()
                            .content())
                    .trim();
            refinedResponse.setChainOfThought(solution);
            chainOfThought = evaluate(solution, task);
        }

        refinedResponse.setSolution(solution);
        return refinedResponse;
    }

    private String evaluate(String solution, String task) {
        return Objects.requireNonNull(chatClient.prompt("Think step by step. Evaluate the following solution: "
                                + solution + " for task: " + task + ". Reply with PASS if solution is correct, otherwise reply with NEEDS_IMPROVEMENT.")
                        .call()
                        .content())
                .trim();
    }
}
