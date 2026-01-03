package com.example.travelx;

import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;

import java.util.*;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import static org.mapdb.Serializer.STRING;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.data.message.ChatMessage;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

@Configuration
public class LangchainConfig {

    @Value("${openai.api-key}")
    private String apiKey;
    private OpenAiChatModel model;
    private Assistant assistant;
    private MessageWindowChatMemory chatMemory;

    @Bean
    public OpenAiChatModel openAiChatModel() {
        this.model = OpenAiChatModel.builder()
            .apiKey(apiKey)
            .modelName(GPT_4_O_MINI)
            .responseFormat("json_schema")
            .strictJsonSchema(true)
            .logRequests(true)
            .logResponses(true)
            .build();
        return this.model;
    }

    private MessageWindowChatMemory messageWindowChatMemory() {
        this.chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(new PersistentChatMemoryStore())
                .build();
        return this.chatMemory;
    }

    @Bean
    public Assistant openAiAssistant() {
        this.chatMemory = this.messageWindowChatMemory();
        this.model = this.openAiChatModel();
        this.assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(this.model)
                .chatMemory(this.chatMemory)
                .build();
        return this.assistant;
    }

    static class PersistentChatMemoryStore implements ChatMemoryStore {

        private final DB db = DBMaker.fileDB("chat-memory.db").transactionEnable().make();
        private final Map<String, String> map = db.hashMap("messages", STRING, STRING).createOrOpen();

        @Override
        public List<ChatMessage> getMessages(Object memoryId) {
            String json = map.get((String) memoryId);
            return messagesFromJson(json);
        }

        @Override
        public void updateMessages(Object memoryId, List<ChatMessage> messages) {
            String json = messagesToJson(messages);
            map.put((String) memoryId, json);
            db.commit();
        }

        @Override
        public void deleteMessages(Object memoryId) {
            map.remove((String) memoryId);
            db.commit();
        }
    }
}