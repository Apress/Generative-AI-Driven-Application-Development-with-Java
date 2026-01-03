package com.example.travelx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;


@Configuration
public class ServiceWithPersistentMemory {
    private Assistant assistant;
    private OpenAiChatModel chatModel;

    @Bean
    public Assistant chatAssistant(Assistant assistant, OpenAiChatModel chatModel) {
        this.assistant = assistant;
        this.chatModel = chatModel;
        return this.assistant;
    }

    public Aircraft chat(String message) {
        String response = this.assistant.chat(message);
        AircraftExtractor aircraftExtractor = AiServices.create(AircraftExtractor.class, chatModel); // see [1] below
        Aircraft aircraft = aircraftExtractor.extractAircraftFrom(response);
        return aircraft;
    }
}