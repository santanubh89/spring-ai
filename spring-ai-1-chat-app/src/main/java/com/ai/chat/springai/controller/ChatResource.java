package com.ai.chat.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class ChatResource {

    private final ChatClient chatClient;

    public ChatResource(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    // Very first API to call Open AI

    // curl --location 'http://localhost:8080/dad-jokes?message=Tell%20me%20a%20joke%20about%20cat'
    // curl --location 'http://localhost:8080/dad-jokes'
    @GetMapping("/dad-jokes")
    public String chatApi(@RequestParam(value = "message", defaultValue = "Tell me a dad joke") String message) {
        return chatClient.prompt().user(message).call().content();
        // return chatClient.prompt(new Prompt(message)).call().content();
        // return chatClient.prompt(message).call().content();
    }

    @GetMapping("/tech-joke")
    public String jokes() {
        var systemMessage = new SystemMessage("""
                Your primary function is to tell tech jokes.
                If someone asks you for any other type of joke, you should respond that you only know tech jokes.
                """);
        var userMessage = new UserMessage("Tell me a serious joke about cats");
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        return chatResponse.getResults().get(0).getOutput().getText();
    }

    @GetMapping("/popular")
    public String findPopularYoutubeByGenre(@RequestParam(value = "genre", defaultValue = "tech") String genre) {
        String message = """
                List 10 of the most popular YouTubers in {genre} along with their current subscriber count.
                    If you dont know the answer, just say 'I don't know' 
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre));
        ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
        return response.getResults().get(0).getOutput().getText();
    }

    @Value("classpath:/prompts/Youtube.st")
    private Resource youtubePromptResource;

    @GetMapping("/popular-resource")
    public String findPopularYoutubeByGenre2(@RequestParam(value = "genre", defaultValue = "tech") String genre) {
        PromptTemplate promptTemplate = new PromptTemplate(youtubePromptResource);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre));
        ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
        return response.getResult().getOutput().getText();
    }


}
