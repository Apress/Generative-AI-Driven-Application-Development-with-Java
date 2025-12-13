package com.example.demo;

import com.example.demo.workflow.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

record ActorFilms(String actor, List<String> movies) {}

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(OpenAiChatModel openAiChatModel) {
		return args -> {
			ChatClient chatClient = ChatClient.builder(openAiChatModel).build();
			String response = chain(
					"What is 2+2?",
					new String[]{
							"Think through it.",
							"Validate if the results is scientifically presented.",
							"Share only the output."
					},
					chatClient);
			System.out.println("Response: " + response);
			System.out.println("Parallelization Workflow:");
			List<String> parallelResponse = parallel(chatClient);
			parallelResponse.forEach(System.out::println);
			System.out.println("Routing Workflow:");
			String routeResponse = route(chatClient);
			System.out.println("Response: " + routeResponse);
			System.out.println("Orchestrator Workers Workflow:");
			orchestrate(chatClient);
			System.out.println("Evaluator Optimizer Workflow:");
			evaluatorOptimizer(chatClient);

		};
	}

	private void evaluatorOptimizer(ChatClient chatClient) {
		EvaluatorOptimizerWorkflow workflow = new EvaluatorOptimizerWorkflow(chatClient);

		RefinedResponse response = workflow.loop(
				"Create a Java class implementing a thread-safe counter"
		);

		System.out.println("Final Solution: " + response.solution());
		System.out.println("Evaluation: " + response.chainOfThought());
	}

	private void orchestrate(ChatClient chatClient) throws JsonProcessingException {
		OrchestratorWorkersWorkflow workflow = new OrchestratorWorkersWorkflow(chatClient);
		WorkerResponse response = workflow.process(
				"Generate both technical and user-friendly documentation for a REST API endpoint"
		);

		System.out.println("Analysis: " + response.analysis());
		System.out.println("Worker Outputs: " + response.workerResponses());
	}

	private String route(ChatClient chatClient) {
		RoutingWorkflow workflow = new RoutingWorkflow(chatClient);
		Map<String, String> routes = Map.of(
				"billing", "You are a billing specialist. Help resolve billing issues...",
				"technical", "You are a technical support engineer. Help solve technical problems...",
				"general", "You are a customer service representative. Help with general inquiries..."
		);

		String input = "My account was charged twice last week";
		return workflow.route(input, routes, chatClient);
	}

	private List<String> parallel(ChatClient chatClient) {
		return new ParallelizationWorkflow(chatClient)
				.parallel(
						"Analyze how market changes will impact %s stakeholder group.",
						List.of(
								"Customers",
								"Employees",
								"Investors",
								"Suppliers"
						)
				);
	}

	private String chain(String userInput, String[] systemPrompts, ChatClient chatClient) {
		String response = userInput;

		for (String prompt : systemPrompts) {
			String input = String.format("%s\n %s", prompt, response);
			response = chatClient.prompt(input).call().content();
		}

		return response;
	}
}
