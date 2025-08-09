package com.ai.chat.springai3rag.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/faq")
public class OlympicsFAQResource {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    @Value("classpath:/prompts/rag-prompt-template.st")
    private Resource olympicPrompt;

    public OlympicsFAQResource(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    @GetMapping
    public String faq(@RequestParam(value = "message",
            defaultValue = "How can I buy tickets for the olympic games Paris 2024")
                          String message) {
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(2).build());
        List<String> contentList = similarDocuments.stream().map(Document::getText).toList();
        PromptTemplate promptTemplate = new PromptTemplate(olympicPrompt);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", message);
        promptParameters.put("documents", String.join(",", contentList));
        Prompt prompt = promptTemplate.create(promptParameters);

        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        return chatResponse.getResult().getOutput().getText();
    }

}
