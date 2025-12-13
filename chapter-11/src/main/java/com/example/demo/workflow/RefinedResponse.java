package com.example.demo.workflow;

public class RefinedResponse {
    private String chainOfThought = "";
    private String solution;

    public String solution() {
        return solution;
    }

    public String chainOfThought() {
        return chainOfThought;
    }

    public void setChainOfThought(String chainOfThought) {
        this.chainOfThought += chainOfThought;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
