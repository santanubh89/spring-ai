package com.ai.springai.springai6gpt4o;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatModal {

    private final ChatClient chatClient;

    public ChatModal(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/jokes")
    public String jokes(@RequestParam(value = "topic", defaultValue = "Dogs") String topic) {
        PromptTemplate promptTemplate = new PromptTemplate("Tell me a joke about {topic}");
        Prompt prompt = promptTemplate.create(Map.of("topic", topic));
        return chatClient.prompt(prompt).call().content();
    }

}
