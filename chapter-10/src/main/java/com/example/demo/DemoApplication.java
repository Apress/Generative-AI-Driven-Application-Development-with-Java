package com.example.demo;

import com.example.demo.tools.MyTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(OpenAiChatModel openAiChatModel) {
		return args -> {
			ChatClient chatClient = ChatClient.builder(openAiChatModel).build();
			String response1 = chatClient
					.prompt("what is the day today?")
					.tools(new MyTools())
					.call()
					.content();

			String response2 = chatClient
					.prompt("what day is tomorrow?")
					.tools(new MyTools())
					.call()
					.content();

			String response3 = chatClient
					.prompt("what is the day today and Can you set an alarm 10 minutes from now?")
					.tools(new MyTools())
					.call()
					.content();

			String response4 = chatClient
					.prompt("How is the weather today at lat and long of 48.8584° N, 2.2945° E?")
					.tools(new MyTools())
					.call()
					.content();

			System.out.println("Output1: " + response1);
			System.out.println("Output2: " + response2);
			System.out.println("Output3: " + response3);
			System.out.println("Output4: " + response4);
		};
	}
}
