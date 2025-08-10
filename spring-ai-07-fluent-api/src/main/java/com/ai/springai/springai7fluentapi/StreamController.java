package com.ai.springai.springai7fluentapi;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class StreamController {

    private final ChatClient chatClient;

    public StreamController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/stream")
    public Flux<String> stream() {
        return chatClient.prompt().user("""
                I am travelling to Kansas City next week.
                What are top 10 barbeque joints in the city?""")
                .stream().content();
    }
}
