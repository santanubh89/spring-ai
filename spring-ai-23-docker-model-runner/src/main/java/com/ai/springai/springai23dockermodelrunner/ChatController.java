package com.ai.springai.springai23dockermodelrunner;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        chatClient = chatClientBuilder.build();
    }

    //http://localhost:8080/?query=What%20is%20Docker?
    @GetMapping
    public String chat(@RequestParam String query) {
        return chatClient.prompt().user(query).call().content();
    }


}
