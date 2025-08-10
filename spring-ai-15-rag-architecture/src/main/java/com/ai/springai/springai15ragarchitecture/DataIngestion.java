package com.ai.springai.springai15ragarchitecture;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class DataIngestion {

    public static final Logger log = LoggerFactory.getLogger(DataIngestion.class);

    private final JdbcClient jdbcClient;

    private final VectorStore vectorStore;

    @Value("classpath:/docs/article_thebeatsept2024.pdf")
    private Resource marketResearchData;

    public DataIngestion(VectorStore vectorStore, JdbcClient jdbcClient) {
        this.vectorStore = vectorStore;
        this.jdbcClient = jdbcClient;
    }

    @PostConstruct
    public void init() {
        Integer count = jdbcClient.sql("select count(*) from vector_store")
                .query(Integer.class).single();
        log.info("Current data size in the vector store: {}", count);
        if (count == 0) {
            log.info("Loading Spring Boot reference PDF into VectorStore");
            PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(0)
                            .build())
                    .withPagesPerDocument(1).build();
            ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader(marketResearchData, config);
            var textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(pdfReader.get()));
            log.info("PDF data loaded to vector store");
            count = jdbcClient.sql("select count(*) from vector_store")
                    .query(Integer.class).single();
            log.info("Current data size in the vector store after loading: {}", count);
        }
    }
}
