package com.app.ai.springai11multiplellm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnthropicChatController {

    private final ChatClient chatClient;

    public AnthropicChatController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/anthropic")
    public String anthropicChat() {
        return chatClient.prompt("Tell me a joke").call().content();
    }
}
