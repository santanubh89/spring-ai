package com.app.ai.springai11multiplellm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenAIChatController {

    private final ChatClient chatClient;

    public OpenAIChatController(@Qualifier("openAIChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/openai")
    public String openAIChat() {
        return chatClient.prompt("Tell me a joke").call().content();
    }


}
