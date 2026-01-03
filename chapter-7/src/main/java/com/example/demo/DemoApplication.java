package com.example.demo;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.testcontainers.milvus.MilvusContainer;

@SpringBootApplication
public class DemoApplication {

	public static final int EMBEDDING_DIMENSION = 1536;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Value("${spring.ai.openai.api-key}")
	private String apiKey;

	@Bean
	public CommandLineRunner runner(ChatClient.Builder builder) {
		return args -> {
			ChatClient chatClient = builder.build();
			String response = chatClient.prompt("Tell me a joke").call().content();
			System.out.println(response);
		};
	}

	@Bean
	public OpenAiChatModel chatModel() {
		OpenAiApi openAiApi = new OpenAiApi(apiKey);
		return new OpenAiChatModel(openAiApi);
	}

	@Bean
	public VectorStore vecStore(MilvusServiceClient milvusClient, MarkdownDocumentReader markdownDocumentReader) {
		OpenAiApi openAiApi = new OpenAiApi(apiKey);
		OpenAiEmbeddingModel embeddingModel = new OpenAiEmbeddingModel(
				openAiApi,
				MetadataMode.EMBED,
				OpenAiEmbeddingOptions.builder()
						.model("text-embedding-ada-002")
						.user("user-6")
						.build(),
				RetryUtils.DEFAULT_RETRY_TEMPLATE);

		MilvusVectorStore vectorStore = MilvusVectorStore.builder(milvusClient, embeddingModel)
				.collectionName("vector_store")
				.databaseName("default")
				.indexType(IndexType.IVF_FLAT)
				.metricType(MetricType.COSINE)
				.batchingStrategy(new TokenCountBatchingStrategy())
				.initializeSchema(true)
				.build();

		vectorStore.add(markdownDocumentReader.read());
		return vectorStore;
	}

	@Bean
	public MilvusContainer milvusContainer() {
		MilvusContainer container = new MilvusContainer("milvusdb/milvus:latest");
		container.start();
		return container;
	}

	@Bean
	public MilvusServiceClient milvusClient(MilvusContainer milvusContainer) {
		return new MilvusServiceClient(ConnectParam.newBuilder()
				.withAuthorization("minioadmin", "minioadmin")
				.withUri(milvusContainer.getEndpoint())
				.build());
	}

	@Bean
	public MarkdownDocumentReader documentReader(@Value("classpath:flights.md") Resource resource) {
		MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
				.withHorizontalRuleCreateDocument(true)
				.withIncludeCodeBlock(false)
				.withIncludeBlockquote(false)
				.withAdditionalMetadata("filename", "flights.md")
				.build();

		return new MarkdownDocumentReader(resource, config);
	}
}