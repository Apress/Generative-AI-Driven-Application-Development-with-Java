package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	private ChatClient chatClient;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(ChatClient.Builder chatClientBuilder, ChatModel chatModel) {
		chatClient = chatClientBuilder.build();

		return args -> {
			makeChatClientRequest();
			makeChatModelRequest(chatModel);
		};
	}

	private void makeChatClientRequest() {
		String response = chatClient.prompt("You are a professional comedian. Tell a joke.")
				.user("Tell me a joke.")
				.call()
				.content();
		System.out.println(response);
	}

	private void makeChatModelRequest(ChatModel chatModel) {
		String response = chatModel.call(
				new Prompt("Tell me a joke.")
		).getResult().getOutput().getText();
		System.out.println(response);
	}
}
