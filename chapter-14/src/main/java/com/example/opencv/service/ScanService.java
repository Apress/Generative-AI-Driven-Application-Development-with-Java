package com.example.opencv.service;

import com.example.opencv.model.FlightInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.Objects;

@Service
public class ScanService {

    private final ChatModel chatModel;
    private final ImageModel imageModel;
    private final ObjectMapper objectMapper;

    public ScanService(ChatModel chatModel, ImageModel imageModel) {
        this.chatModel = chatModel;
        this.imageModel = imageModel;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private String generateGreetingImage(String destination) {
        String prompt = """
                As an airline industry, we strive to provide the best experience to our customers.
                After they have booked a flight ticket with us, we want to share a collage of some
                beautiful places present at their destination location to inspire and motivate them.
                Generate an image for our customer who is planning to visit: %s
                """.formatted(destination);
        OpenAiImageOptions openAiImageOptions = OpenAiImageOptions.builder()
                .withQuality("hd")
                .withN(1)
                .withHeight(1024)
                .withWidth(1024)
                .build();
        ImagePrompt imagePrompt = new ImagePrompt(prompt, openAiImageOptions);
        ImageResponse response = imageModel.call(imagePrompt);
        return response.getResult().getOutput().getUrl().trim();
    }

    public FlightInfo scanImage(Resource imageFile) throws JsonProcessingException {
        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(ResponseFormat.Type.JSON_OBJECT).build();
        UserMessage userMessage = new UserMessage("""
                Scan the information in the Flight Ticket.
                Return in json format with keys like following example:
                {
                    "name": "Jane Doe",
                    "flightNumber": "XY1234",
                    "origin": "SFO",
                    "destination": "LHR",
                    "date": "2025-06-01",
                    "time": "14:45:00",
                    "seatNumber": "21C"}
                """,
                new Media(MimeTypeUtils.IMAGE_JPEG, imageFile));
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O_MINI.getValue())
                .responseFormat(responseFormat)
                .build();
        ChatResponse response = chatModel.call(new Prompt(userMessage, openAiChatOptions));
        String jsonResponse = Objects.requireNonNull(
                response.getResult().getOutput().getText()).trim();

        FlightInfo flightInfo = objectMapper.readValue(jsonResponse, FlightInfo.class);
        String greetingImageUrl = this.generateGreetingImage(flightInfo.getDestination());
        flightInfo.setGreetingImageUrl(greetingImageUrl);

        return flightInfo;
    }
}

