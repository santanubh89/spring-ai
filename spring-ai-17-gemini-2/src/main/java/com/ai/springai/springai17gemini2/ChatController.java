package com.ai.springai.springai17gemini2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
public class ChatController {

    public static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Value("${spring.ai.openai.api-key}")
    private String GEMINI_API_KEY;

    private final RestClient restClient;

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder, RestClient.Builder restClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.restClient = restClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com").build();
    }

    @GetMapping
    public String chat() {
        return chatClient.prompt().user("Tell me an interesting fact about google")
                .call().content();
    }

    @GetMapping("/gemini")
    public List<GeminiModel> models() {
        ResponseEntity<ModelListResponse> response = restClient.get().uri("/v1beta/openai/models")
                .header("Authorization", "Bearer " + GEMINI_API_KEY)
                .retrieve().toEntity(ModelListResponse.class);
        return response.getBody().data();
    }

}
