package com.ai.chat.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/olympics")
public class OlympicsController {

    @Value("classpath:/prompts/OlympicSport.st")
    private Resource olympicsSportsPrompt;

    @Value("classpath:/docs/olympic-sports.txt")
    private Resource olympicsSportsDocument;

    private final ChatClient chatClient;

    public OlympicsController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    // http://localhost:8080/olympics/2024?stuffit=true
    @GetMapping("/2024")
    public String getOlympicsInfo(
            @RequestParam(value = "message", defaultValue = "Name the sports included in Summer Olympics 2024.") String message,
            @RequestParam(value = "stuffit", defaultValue = "false") boolean stuffit) {
        PromptTemplate promptTemplate = new PromptTemplate(olympicsSportsPrompt);
        Map<String, Object> map = new HashMap<>();
        map.put("question", message);
        if (stuffit) {
            map.put("context", olympicsSportsDocument);
        } else {
            map.put("context", "");
        }
        Prompt prompt = promptTemplate.create(map);
        ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
        return response.getResult().getOutput().getText();
    }

}
