package com.example.demo.workflow;

public class WorkerResponse {
    private String workerResponses = "";
    private String analysis = "";

    public String analysis() {
        return analysis;
    }

    public String workerResponses() {
        return workerResponses;
    }

    public void setWorkerResponses(String workerResponses) {
        this.workerResponses += "\n " + workerResponses;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
