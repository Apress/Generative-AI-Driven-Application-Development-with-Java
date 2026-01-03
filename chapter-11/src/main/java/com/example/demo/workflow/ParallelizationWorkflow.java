package com.example.demo.workflow;

import org.springframework.ai.chat.client.ChatClient;

import java.util.List;

public class ParallelizationWorkflow {
    private final ChatClient chatClient;

    public ParallelizationWorkflow(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public List<String> parallel(String prompt, List<String> stakeholders) {
        return stakeholders.parallelStream()
                .map(stakeholder -> this.chatClient.prompt(String.format(prompt, stakeholder)).call().content())
                .toList();
    }
}
