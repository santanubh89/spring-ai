package com.ai.springai.springai4ragpgvector.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpringAssistantService {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    @Value("classpath:/prompts/spring-boot-reference.st")
    private Resource springPromptTemplate;

    public SpringAssistantService(ChatClient.Builder clientBuilder, VectorStore vectorStore) {
        this.chatClient = clientBuilder.build();
        this.vectorStore = vectorStore;
    }


    public String handleQuery(@DefaultValue(value = "What is Spring Boot") String message) {
        PromptTemplate promptTemplate = new PromptTemplate(springPromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", message);
        promptParameters.put("documents", getContext(message));
        Prompt prompt = promptTemplate.create(promptParameters);
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        return chatResponse.getResult().getOutput().getText();
    }

    private String getContext(String message) {
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(3).build());
        List<String> similarDocumentList = similarDocuments.stream().map(Document::getText).collect(Collectors.toList());
        return String.join("\n", similarDocumentList);
    }

}
