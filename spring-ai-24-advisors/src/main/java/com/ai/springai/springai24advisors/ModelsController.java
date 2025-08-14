package com.ai.springai.springai24advisors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelsController {

    private final ChatClient chatClient;

    public ModelsController(ChatClient.Builder chatClientBuilder,
                            VectorStore vectorStore) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).order(0).build(),
                        new CustomLoggingAdvisor())
                .build();
    }

    @GetMapping("/rag/models")
    public Models models(@RequestParam(value = "message",
            defaultValue = "Give me a list of all the models from OpenAI along with their context window.")
                         String message) {
        return chatClient.prompt().user(message).call().entity(Models.class);
    }

}
