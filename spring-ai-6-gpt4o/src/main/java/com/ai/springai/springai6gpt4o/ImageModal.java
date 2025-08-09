package com.ai.springai.springai6gpt4o;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ImageModal {

    private final ChatClient chatClient;

    public ImageModal(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/describe")
    public String describeImage() {
        ClassPathResource classPathResource = new ClassPathResource("/images/coffee.jpg");
        UserMessage userMessage = UserMessage.builder()
                .text("Can you please explain what you see in the following image?")
                .media(new Media(MimeTypeUtils.IMAGE_JPEG, classPathResource)).build();
        Prompt prompt = new Prompt(userMessage);
        return chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/explain")
    public String describeCode() {
        ClassPathResource classPathResource = new ClassPathResource("/images/code.png");
        UserMessage userMessage = UserMessage.builder()
                .text("Following is a screenshot of some code. Can you do your best to provide a description of what the code is doing?")
                .media(new Media(MimeTypeUtils.IMAGE_PNG, classPathResource)).build();
        Prompt prompt = new Prompt(userMessage);
        return chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/image-to-code")
    public String generateCode() {
        ClassPathResource classPathResource = new ClassPathResource("/images/code.png");
        UserMessage userMessage = UserMessage.builder()
                .text("""
                Following is a screenshot of a code.
                Can you translate this from image into actual code?
                """)
                .media(new Media(MimeTypeUtils.IMAGE_PNG, classPathResource)).build();
        Prompt prompt = new Prompt(userMessage);
        return chatClient.prompt(prompt).call().content();
    }

}
