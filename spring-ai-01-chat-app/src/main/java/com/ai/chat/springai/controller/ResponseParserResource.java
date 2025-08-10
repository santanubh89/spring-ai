package com.ai.chat.springai.controller;

import com.ai.chat.springai.model.Author;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

// Response Parser

@RestController
public class ResponseParserResource {

    private final ChatClient chatClient;

    public ResponseParserResource(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/songs")
    public String findSongsByArtist(@RequestParam(value = "artist", defaultValue = "Linkin Park") String artist) {
        String message = """
                Please give me a list of top 10 songs for the artist {artist}. If you don't know the answer, just say "I don't Know".
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("artist", artist));
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        return Objects.requireNonNull(chatResponse).getResult().getOutput().getText();
    }

    @GetMapping("/songs-list")
    public List<String> findSongsListByArtist(@RequestParam(value = "artist", defaultValue = "Linkin Park") String artist) {
        String message = """
                Please give me a list of top 10 songs for the artist {artist}. If you don't know the answer, just say "I don't Know".
                {format}
                """;
        ListOutputConverter outputConverter = new ListOutputConverter(new DefaultConversionService());
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("artist", artist, "format", outputConverter.getFormat()));
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        String text = chatResponse.getResults().get(0).getOutput().getText();
        return outputConverter.convert(text);
    }

    @GetMapping("/social/{author}")
    public Map<String, Object> findAuthorSocialNetwork(@PathVariable("author") String author) {
        String message = """
                Generate a list of links for the author {author}.
                Include the authors name as the key and any social network links as the object.
                {format}.
                """;
        MapOutputConverter outputConverter = new MapOutputConverter();
        String format = outputConverter.getFormat();
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("author", author, "format", format));
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        String text = chatResponse.getResults().get(0).getOutput().getText();
        return outputConverter.convert(text);
    }

    @GetMapping("/book-list/{author}")
    public Author getBooksByAuthor(@PathVariable String author) {
        String message = """
                Generate a list of books written by the author {author}.
                If you aren't positive that a book belongs to this author, please don't include it.
                {format}.
                """;
        BeanOutputConverter<Author> outputConverter = new BeanOutputConverter<Author>(Author.class);
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("author", author, "format", outputConverter.getFormat()));
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        String text = chatResponse.getResults().get(0).getOutput().getText();
        return outputConverter.convert(text);
    }

}
