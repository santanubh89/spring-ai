package com.ai.springai.springai7fluentapi;

import com.ai.springai.springai7fluentapi.output.Actor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/actor")
public class ActorController {

    private final ChatClient chatClient;

    public ActorController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/string")
    public String getActorFilmsString() {
        return chatClient.prompt()
                .user("Generate a filmography for Anthony Hopkins for the year 2010")
                .call().content();
    }

    @GetMapping("/bean")
    public Actor getActorFilmsBean() {
        return chatClient.prompt()
                .user("Generate a filmography for Anthony Hopkins")
                .call().entity(Actor.class);
    }

    @GetMapping("/bean-list")
    public List<Actor> getActorFilmsBeanList() {
        return chatClient.prompt()
                .user("Generate a filmography for actors Denzel Washington, Leonardo DiCaprio, and Tom Hanks")
                .call().entity(new ParameterizedTypeReference<>() {
                });
    }


}
