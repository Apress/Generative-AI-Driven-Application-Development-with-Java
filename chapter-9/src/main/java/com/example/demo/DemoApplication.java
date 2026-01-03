package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

record ActorFilms(String actor, List<String> movies) {}

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	private void lowerValueTemperaturePrompting(ChatClient chatClient) {
		ChatOptions chatOptions = ChatOptions.builder()
				.model("gemma3:1b")
				.maxTokens(100)
				.temperature(0.1)
				.build();

		String result = chatClient.prompt("A short poem on life of ducks in space.")
				.options(chatOptions)
				.call()
				.content();
		System.out.println("Result: " + result);
	}

	private void mediumValueTemperaturePrompting(ChatClient chatClient) {
		ChatOptions chatOptions = ChatOptions.builder()
				.model("gemma3:1b")
				.maxTokens(100)
				.temperature(0.5)
				.build();

		String result = chatClient.prompt("A short poem on life of ducks in space.")
				.options(chatOptions)
				.call()
				.content();
		System.out.println("Result: " + result);
	}

	private void largeValueTemperaturePrompting(ChatClient chatClient) {
		ChatOptions chatOptions = ChatOptions.builder()
				.model("gemma3:1b")
				.maxTokens(100)
				.temperature(1.0)
				.build();

		String result = chatClient.prompt("A short poem on life of ducks in space.")
				.options(chatOptions)
				.call()
				.content();
		System.out.println("Result: " + result);
	}

	private void maxTokenPrompting(ChatClient chatClient) {
		ChatOptions chatOptions = ChatOptions.builder()
				.model("gemma3:1b")
				.maxTokens(500)
				.temperature(1.0)
				.build();

		String result = chatClient.prompt("A poem on life of ducks in space.")
				.options(chatOptions)
				.call()
				.content();
		System.out.println("Result: " + result);
	}

	private void outputFormat(ChatClient chatClient) {
		ActorFilms actorFilms = chatClient
				.prompt("Generate the filmography for a random actor.")
				.call()
				.entity(ActorFilms.class);

		System.out.println("Output: " + actorFilms);
	}

	private void openAiSpecificOptions(ChatClient chatClient) {
		OpenAiChatOptions openAiOptions = OpenAiChatOptions.builder()
				.model("gemma3:1b")
				.temperature(0.2)
				.frequencyPenalty(0.5)      // OpenAI-specific parameter
				.presencePenalty(0.3)       // OpenAI-specific parameter
				.seed(42)                   // OpenAI-specific deterministic generation
				.build();

		String result = chatClient.prompt("Share the stock value of Apple Inc.")
				.options(openAiOptions)
				.call()
				.content();
		System.out.println("Result: " + result);
	}

	private void zeroShotPrompting(ChatClient chatClient) {
		String reviewSentiment = chatClient.prompt("""
				Classify movie reviews as POSITIVE, NEUTRAL or NEGATIVE.
				Review: "Her" is a disturbing study revealing the direction
				humanity is headed if AI is allowed to keep evolving,
				unchecked. I wish there were more movies like this masterpiece.
				Sentiment:
				""")
				.options(ChatOptions.builder()
						.model("gemma3:1b")
						.temperature(0.1)
						.maxTokens(5)
						.build())
				.call()
				.content();
	
		System.out.println("Output: " + reviewSentiment);
	}
	

	private void fewShotPrompting(ChatClient chatClient) {
		String pizzaOrder = chatClient.prompt("""
            Parse a customer's pizza order into valid JSON

            EXAMPLE 1:
            I want a small pizza with cheese, tomato sauce, and pepperoni.
            JSON Response:
            ```
            {
                "size": "small",
                "type": "normal",
                "ingredients": ["cheese", "tomato sauce", "pepperoni"]
            }
            ```

            EXAMPLE 2:
            Can I get a large pizza with tomato sauce, basil and mozzarella.
            JSON Response:
            ```
            {
                "size": "large",
                "type": "normal",
                "ingredients": ["tomato sauce", "basil", "mozzarella"]
            }
            ```

            Now, I would like a large pizza, with the first half cheese and mozzarella.
            And the other tomato sauce, ham and pineapple.
            """)
				.options(ChatOptions.builder()
						.model("gemma3:1b")
						.temperature(0.1)
						.maxTokens(250)
						.build())
				.call()
				.content();

		System.out.println("Output: " + pizzaOrder);
	}

	private void systemPrompting(ChatClient chatClient) {
		String movieReview = chatClient
				.prompt()
				.system("Classify movie reviews as positive, neutral or negative. Only return the label in uppercase.")
				.user("""
                    Review: "Her" is a disturbing study revealing the direction
                    humanity is headed if AI is allowed to keep evolving,
                    unchecked. It's so disturbing I couldn't watch it.

                    Sentiment:
                    """)
				.options(ChatOptions.builder()
						.model("gemma3:1b")
						.temperature(1.0)
						.topP(0.8)
						.maxTokens(5)
						.build())
				.call()
				.content();
		System.out.println("Output: " + movieReview);
	}

	private void systemPrompting2(ChatClient chatClient) {
		record MovieReviews(Movie[] movie_reviews) {
			enum Sentiment {
				POSITIVE, NEUTRAL, NEGATIVE
			}

			record Movie(Sentiment sentiment, String name) {
			}
		}

		MovieReviews movieReviews = chatClient
				.prompt()
				.system("""
                Classify movie reviews as positive, neutral or negative. Return
                valid JSON.
                """)
				.user("""
                Review: "Her" is a disturbing study revealing the direction
                humanity is headed if AI is allowed to keep evolving,
                unchecked. It's so disturbing I couldn't watch it.

                JSON Response:
                """)
				.call()
				.entity(MovieReviews.class);
		System.out.println("Output: " + Arrays.stream(movieReviews.movie_reviews()).findFirst());
	}

	private void rolePrompting(ChatClient chatClient) {
		String travelSuggestions = chatClient
				.prompt()
				.system("""
                    I want you to act as a travel guide. I will write to you
                    about my location and you will suggest 3 places to visit near
                    me. In some cases, I will also give you the type of places I
                    will visit.
                    """)
				.user("""
                    My suggestion: "I am in Amsterdam and I want to visit only museums."
                    Travel Suggestions:
                    """)
				.call()
				.content();
		System.out.println("Output: " + travelSuggestions);
	}

	private void rolePrompting2(ChatClient chatClient) {
		String humorousTravelSuggestions = chatClient
				.prompt()
				.system("""
                    I want you to act as a travel guide. I will write to you about
                    my location and you will suggest 3 places to visit near me in
                    a humorous style.
                    """)
				.user("""
                    My suggestion: "I am in Amsterdam and I want to visit only museums."
                    Travel Suggestions:
                    """)
				.call()
				.content();
		System.out.println("Output: " + humorousTravelSuggestions);
	}

	private void contextualPrompting(ChatClient chatClient) {
		String articleSuggestions = chatClient
				.prompt()
				.user(u -> u.text("""
                    Suggest 3 topics to write an article about with a few lines of
                    description of what this article should contain.

                    Context: {context}
                    """)
						.param("context", "You are writing for a blog about retro 80's arcade video games."))
				.call()
				.content();
		System.out.println("Output: " + articleSuggestions);
	}

	private void stepBackPrompting(ChatClient chatClient) {
		// First get high-level concepts
		String stepBack = chatClient
				.prompt("""
                    Based on popular first-person shooter action games, what are
                    5 fictional key settings that contribute to a challenging and
                    engaging level storyline in a first-person shooter video game?
                    """)
				.call()
				.content();

		// Then use those concepts in the main task
		String story = chatClient
				.prompt()
				.user(u -> u.text("""
                    Write a one paragraph storyline for a new level of a first-
                    person shooter video game that is challenging and engaging.

                    Context: {step-back}
                    """)
						.param("step-back", stepBack))
				.call()
				.content();
		System.out.println("Output: " + story);
	}

	private void chainOfThoughtZeroShotPrompting(ChatClient chatClient) {
		String output = chatClient
				.prompt("""
                    When I was 3 years old, my partner was 3 times my age. Now,
                    I am 20 years old. How old is my partner?

                    Let's think step by step.
                    """)
				.call()
				.content();
		System.out.println("Output: " + output);
	}

	private void chainOfThoughtSingleShotPrompting(ChatClient chatClient) {
		String output = chatClient
				.prompt("""
                    Q: When my brother was 2 years old, I was double his age. Now
                    I am 40 years old. How old is my brother? Let's think step
                    by step.
                    A: When my brother was 2 years, I was 2 * 2 = 4 years old.
                    That's an age difference of 2 years and I am older. Now I am 40
                    years old, so my brother is 40 - 2 = 38 years old. The answer
                    is 38.
                    Q: When I was 3 years old, my partner was 3 times my age. Now,
                    I am 20 years old. How old is my partner? Let's think step
                    by step.
                    A:
                    """)
				.call()
				.content();
		System.out.println("Output: " + output);
	}

	private void selfConsistencyPrompting(ChatClient chatClient) {
		String email = """
            Hi,
            I have seen you use Wordpress for your website. A great open
            source content management system. I have used it in the past
            too. It comes with lots of great user plugins. And it's pretty
            easy to set up.
            I did notice a bug in the contact form, which happens when
            you select the name field. See the attached screenshot of me
            entering text in the name field. Notice the JavaScript alert
            box that I inv0k3d.
            But for the rest it's a great website. I enjoy reading it. Feel
            free to leave the bug in the website, because it gives me more
            interesting things to read.
            Cheers,
            Harry the Hacker.
            """;

		record EmailClassification(Classification classification, String reasoning) {
			enum Classification {
				IMPORTANT, NOT_IMPORTANT
			}
		}

		int importantCount = 0;
		int notImportantCount = 0;

		// Run the model 5 times with the same input
		for (int i = 0; i < 5; i++) {
			EmailClassification output = chatClient
					.prompt()
					.user(u -> u.text("""
                        Email: {email}
                        Classify the above email as IMPORTANT or NOT IMPORTANT. Let's
                        think step by step and explain why.
                        """)
							.param("email", email))
					.options(ChatOptions.builder()
							.temperature(1.0)  // Higher temperature for more variation
							.build())
					.call()
					.entity(EmailClassification.class);

			// Count results
			if (output.classification() == EmailClassification.Classification.IMPORTANT) {
				importantCount++;
			} else {
				notImportantCount++;
			}
		}

		// Determine the final classification by majority vote
		String finalClassification = importantCount > notImportantCount ?
				"IMPORTANT" : "NOT IMPORTANT";
		System.out.println("Important Count: " + importantCount);
		System.out.println("Not Important Count: " + notImportantCount);
		System.out.println("Final Classification: " + finalClassification);
	}

	private void treeOfThoughtPrompting(ChatClient chatClient) {
		// Step 1: Generate multiple initial moves
		String initialMoves = chatClient
				.prompt("""
                    You are playing a game of chess. The board is in the starting position.
                    Generate 3 different possible opening moves. For each move:
                    1. Describe the move in algebraic notation
                    2. Explain the strategic thinking behind this move
                    3. Rate the move's strength from 1-10
                    """)
				.options(ChatOptions.builder()
						.temperature(0.7)
						.build())
				.call()
				.content();

		// Step 2: Evaluate and select the most promising move
		String bestMove = chatClient
				.prompt()
				.user(u -> u.text("""
                    Analyze these opening moves and select the strongest one:
                    {moves}
                    
                    Explain your reasoning step by step, considering:
                    1. Position control
                    2. Development potential
                    3. Long-term strategic advantage
                    
                    Then select the single best move.
                    """).param("moves", initialMoves))
				.call()
				.content();

		// Step 3: Explore future game states from the best move
		String gameProjection = chatClient
				.prompt()
				.user(u -> u.text("""
                    Based on this selected opening move:
                    {best_move}
                    
                    Project the next 3 moves for both players. For each potential branch:
                    1. Describe the move and counter-move
                    2. Evaluate the resulting position
                    3. Identify the most promising continuation
                    
                    Finally, determine the most advantageous sequence of moves.
                    """).param("best_move", bestMove))
				.call()
				.content();
		System.out.println("Game Projection: " + gameProjection);
	}

	private void automaticPrompting(ChatClient chatClient) {
		// Generate variants of the same request
		String orderVariants = chatClient
				.prompt("""
                    We have a band merchandise t-shirt webshop, and to train a
                    chatbot we need various ways to order: "One Metallica t-shirt
                    size S". Generate 10 variants, with the same semantics but keep
                    the same meaning.
                    """)
				.options(ChatOptions.builder()
						.temperature(1.0)  // High temperature for creativity
						.build())
				.call()
				.content();

		// Evaluate and select the best variant
		String output = chatClient
				.prompt()
				.user(u -> u.text("""
                    Please perform BLEU (Bilingual Evaluation Understudy) evaluation on the following variants:
                    ----
                    {variants}
                    ----

                    Select the instruction candidate with the highest evaluation score.
                    """).param("variants", orderVariants))
				.call()
				.content();
		System.out.println("Best Variant: " + output);
	}

	private void codePrompting(ChatClient chatClient) {
		String bashScript = chatClient
				.prompt("""
                    Write a code snippet in Bash, which asks for a folder name.
                    Then it takes the contents of the folder and renames all the
                    files inside by prepending the name draft to the file name.
                    """)
				.options(ChatOptions.builder()
						.temperature(0.1)  // Low temperature for deterministic code
						.build())
				.call()
				.content();
		System.out.println("Bash Script: " + bashScript);
	}

	private void promptExplaining(ChatClient chatClient) {
		String code = """
            #!/bin/bash
            echo "Enter the folder name: "
            read folder_name
            if [ ! -d "$folder_name" ]; then
            echo "Folder does not exist."
            exit 1
            fi
            files=( "$folder_name"/* )
            for file in "${files[@]}"; do
            new_file_name="draft_$(basename "$file")"
            mv "$file" "$new_file_name"
            done
            echo "Files renamed successfully."
            """;

		String explanation = chatClient
				.prompt()
				.user(u -> u.text("""
                    Explain to me the below Bash code:
                    ```
                    {code}
                    ```
                    """).param("code", code))
				.call()
				.content();
		System.out.println("Code Explanation: " + explanation);
	}

	private void promptTranslate(ChatClient chatClient) {
		String bashCode = """
            #!/bin/bash
            echo "Enter the folder name: "
            read folder_name
            if [ ! -d "$folder_name" ]; then
            echo "Folder does not exist."
            exit 1
            fi
            files=( "$folder_name"/* )
            for file in "${files[@]}"; do
            new_file_name="draft_$(basename "$file")"
            mv "$file" "$new_file_name"
            done
            echo "Files renamed successfully."
            """;

		String pythonCode = chatClient
				.prompt()
				.user(u -> u.text("""
                    Translate the below Bash code to a Python snippet:                        
                    {code}                        
                    """).param("code", bashCode))
				.call()
				.content();
		System.out.println("Python Code: " + pythonCode);
	}

	@Bean
	public CommandLineRunner commandLineRunner(OpenAiChatModel openAiChatModel) {
		return args -> {
			ChatClient chatClient = ChatClient.builder(openAiChatModel).build();
			lowerValueTemperaturePrompting(chatClient);
			mediumValueTemperaturePrompting(chatClient);
			largeValueTemperaturePrompting(chatClient);
			maxTokenPrompting(chatClient);
			outputFormat(chatClient);
			openAiSpecificOptions(chatClient);
			zeroShotPrompting(chatClient);
			fewShotPrompting(chatClient);
			systemPrompting(chatClient);
			systemPrompting2(chatClient);
			rolePrompting(chatClient);
			rolePrompting2(chatClient);
			contextualPrompting(chatClient);
			stepBackPrompting(chatClient);
			chainOfThoughtZeroShotPrompting(chatClient);
			chainOfThoughtSingleShotPrompting(chatClient);
			selfConsistencyPrompting(chatClient);
			treeOfThoughtPrompting(chatClient);
			automaticPrompting(chatClient);
			codePrompting(chatClient);
			promptExplaining(chatClient);
			promptTranslate(chatClient);
		};
	}
}
