package io.demo.langchain4j.sample.rag;

import java.util.function.Supplier;

import jakarta.enterprise.context.ApplicationScoped;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import io.quarkiverse.langchain4j.redis.RedisEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class RetrievalAugmentorExample implements Supplier<RetrievalAugmentor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrievalAugmentorExample.class);

    private final RetrievalAugmentor augmentor;

    RetrievalAugmentorExample(RedisEmbeddingStore store, EmbeddingModel model) {
        LOGGER.info("Creating RetrievalAugmentorExample");
        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(model)
                .embeddingStore(store)
                .maxResults(3)
                .build();
        augmentor = DefaultRetrievalAugmentor
                .builder()
                .contentRetriever(contentRetriever)
                .build();
        LOGGER.info("Retrieval AugmentorExample created");
    }

    @Override
    public RetrievalAugmentor get() {
        return augmentor;
    }

}
