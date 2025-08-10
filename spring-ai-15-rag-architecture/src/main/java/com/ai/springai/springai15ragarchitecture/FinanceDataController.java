package com.ai.springai.springai15ragarchitecture;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FinanceDataController {

    private final ChatClient chatClient;

    public FinanceDataController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.defaultAdvisors(new QuestionAnswerAdvisor(vectorStore)).build();
    }

    // curl http://localhost:8080/?query=summarize%20Corporate%20Bond%20Market%20Update
    @GetMapping
    public String getInfo(@RequestParam String query) {
        return chatClient.prompt().user(query).call().content();
    }

}
