package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.*;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private ChatModel chatModel;

	@Autowired
	private VectorStore vectorStore;

	@Test
	void testRelevancyEvaluation() {
		String userText = "What are the flight details for DL606?";
		ChatResponse response = ChatClient.builder(chatModel)
				.build().prompt()
				.advisors(new QuestionAnswerAdvisor(vectorStore))
				.user(userText)
				.call()
				.chatResponse();
        Assertions.assertNotNull(response);
		String responseText = response.getResult().getOutput().getText();
        System.out.println(responseText);
		Evaluator relevancyEvaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));
		List<Document> documents = response.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);
		EvaluationRequest evaluationRequest = new EvaluationRequest(userText, documents, responseText);
		EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
		assertTrue(evaluationResponse.isPass(), "Response is not relevant to the question");

		String wrongAnswer = "We are airline company and provide all sort of flights.";
		evaluationRequest = new EvaluationRequest(userText, documents, wrongAnswer);
		evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
		assertThat(evaluationResponse.isPass()).isFalse();
	}

	@Test
	void testFactCheckingEvaluation() {
		String userText = "What are the flight details for DL606?";
		ChatResponse response = ChatClient.builder(chatModel)
				.build().prompt()
				.advisors(new QuestionAnswerAdvisor(vectorStore))
				.user(userText)
				.call()
				.chatResponse();
		Assertions.assertNotNull(response);
		String responseText = response.getResult().getOutput().getText();
		List<Document> documents = response.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);
		EvaluationRequest evaluationRequest = new EvaluationRequest(userText, documents, responseText);
		Evaluator factCheckingEvaluator = new FactCheckingEvaluator(ChatClient.builder(chatModel));
		EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
		assertThat(evaluationResponse.isPass()).isTrue();

		String wrongAnswer = "DL606 is taking off from New York.";
		evaluationRequest = new EvaluationRequest(userText, documents, wrongAnswer);
		evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
		assertThat(evaluationResponse.isPass()).isFalse();
	}
}
