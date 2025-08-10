package com.ai.springai.springai4ragpgvector.frontend;

import com.ai.springai.springai4ragpgvector.service.SpringAssistantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAssistantResource {

    private final SpringAssistantService service;

    public SpringAssistantResource(SpringAssistantService springAssistantService) {
        this.service = springAssistantService;
    }

    @GetMapping("/spring")
    public String questionCommand(@RequestParam(value = "q", required = false, defaultValue = "What is Spring Boot") String message) {
        return service.handleQuery(message);
    }
}
