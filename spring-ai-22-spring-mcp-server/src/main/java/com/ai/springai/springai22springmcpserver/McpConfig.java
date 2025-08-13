package com.ai.springai.springai22springmcpserver;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpConfig {

    @Bean
    public List<ToolCallback> aiTools(CourseService courseService) {
        return List.of(ToolCallbacks.from(courseService));
    }

}
