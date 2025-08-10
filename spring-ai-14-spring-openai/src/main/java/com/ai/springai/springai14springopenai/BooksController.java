package com.ai.springai.springai14springopenai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BooksController {

    private final ChatClient chatClient;

    public BooksController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping
    public String home() {
        String message = """
                Generate a book recommendation for a book on AI and coding
                """;
        return chatClient.prompt().user(message).call().content();
    }

    @GetMapping("/book")
    public BookRecommendation bookRecommendation() {
        String message = """
                Generate a book recommendation for a book on AI and coding.
                Please limit the summary to 100 words.
                """;
        return chatClient.prompt().user(message).call().entity(BookRecommendation.class);
    }

}
