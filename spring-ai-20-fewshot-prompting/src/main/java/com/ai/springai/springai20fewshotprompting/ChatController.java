package com.ai.springai.springai20fewshotprompting;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.0d)
                        .build())
                .build();
    }

    @GetMapping
    public String chat() {
        SystemMessage systemMessage = new SystemMessage("You are an assistant that classifies tweet sentiment as positive, negative, or neutral.");
        UserMessage userMessage = new UserMessage("""
                Tweet: "I love sunny days!"
                Sentiment: Positive
                
                Tweet: "I lost my wallet today"
                Sentiment: Negative
                
                Tweet: "Just had lunch"
                Sentiment: Neutral
                
                Tweet: "Excited for my trip tomorrow"
                Sentiment:
                """);
        return chatClient.prompt(new Prompt(List.of(systemMessage, userMessage))).call().content();
    }
}
