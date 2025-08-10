package com.ai.springai.springai5functions;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController {

    private final ChatClient chatClient;

    private final WeatherService weatherService;

    public CityController(ChatClient.Builder chatClientBuilder, WeatherService weatherService) {
        this.chatClient = chatClientBuilder.build();
        this.weatherService = weatherService;
    }

    @GetMapping("/cities")
    public String cities(@RequestParam(value = "message") String message) {
        SystemMessage systemMessage = new SystemMessage("You are a helpful AI assistant answering questions about cities around the world");
        UserMessage userMessage = new UserMessage(message);

        ChatOptions chatOptions = OpenAiChatOptions.builder().toolCallbacks(
                List.of(FunctionToolCallback.builder("currentWeather", weatherService)
                        .description("Current Weather Provider")
                        .inputType(WeatherService.Request.class).build())
        ).build();

        ChatResponse chatResponse = chatClient.prompt(new Prompt(List.of(systemMessage, userMessage), chatOptions)).call().chatResponse();
        return chatResponse.getResult().getOutput().getText();
    }

}
