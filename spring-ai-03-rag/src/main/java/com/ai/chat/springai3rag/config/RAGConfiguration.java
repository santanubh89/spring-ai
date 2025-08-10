package com.ai.chat.springai3rag.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RAGConfiguration {

    public static final Logger log = LoggerFactory.getLogger(RAGConfiguration.class);

    @Value("classpath:/docs/olympic-faq.txt")
    private Resource faq;

    @Value("vector-store.json")
    private String vectorStore;

    @Bean
    VectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists()) {
            log.info("vector store file exists");
            vectorStore.load(vectorStoreFile);
        } else {
            log.info("vector store file does not exist, going to create");
            TextReader reader = new TextReader(faq);
            reader.getCustomMetadata().put("filename", "olympic-faq.txt");
            List<Document> documents = reader.read();
            log.info("documents size: {}", documents.size());
            log.info("documents: {}", documents);
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);
            log.info("splitDocuments: {}", splitDocuments);
            vectorStore.add(splitDocuments);
            vectorStore.save(vectorStoreFile);
        }
        return vectorStore;
    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src/main/resources/data");
        String absolutePath = path.toFile().getAbsolutePath()+"/"+vectorStore;
        return new File(absolutePath);

    }

}
