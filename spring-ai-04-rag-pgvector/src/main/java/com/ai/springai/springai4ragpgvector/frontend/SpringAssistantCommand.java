package com.ai.springai.springai4ragpgvector.frontend;

import com.ai.springai.springai4ragpgvector.service.SpringAssistantService;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.shell.command.annotation.Command;

@Command
public class SpringAssistantCommand {

    private final SpringAssistantService service;

    public SpringAssistantCommand(SpringAssistantService springAssistantService) {
        this.service = springAssistantService;
    }

    @Command(command = "q")
    public String questionCommand(@DefaultValue(value = "What is Spring Boot") String message) {
        return service.handleQuery(message);
    }

}
