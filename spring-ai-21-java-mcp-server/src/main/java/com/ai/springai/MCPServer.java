package com.ai.springai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MCPServer {

    public static final Logger log = LoggerFactory.getLogger(MCPServer.class);

    private static PresentationTools presentationTools = new PresentationTools();

    public static void main(String[] args) {

        // Stdio server transport
        var transportProvider = new StdioServerTransportProvider(new ObjectMapper());

        // Sync tool specification
        var syncToolSpecification = getSyncToolSpecification();

        // Create server
        McpServer.sync(transportProvider)
                .serverInfo("javaone-mcp-server", "0.0.1")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(true)
                        .logging()
                        .build())
                .tools(syncToolSpecification)
                .build();

        log.info("Starting MCP Server...");


    }

    private static McpServerFeatures.SyncToolSpecification getSyncToolSpecification() {
        var schema = """
                {
                    "type": "object",
                    "id": "urn:jsonschema:Operation",
                    "properties": {
                        "operation": {
                            "type": "string"
                        }
                    }
                }
                """;
        McpServerFeatures.SyncToolSpecification syncToolSpecification = new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("get_presentations", "Get a list of all presentations from JavaOne", schema),
                (exchange, arguments) -> {
                    // Tool implementation
                    List<Presentation> presentations = presentationTools.getPresentations();
                    List<McpSchema.Content> contents = new ArrayList<>();
                    presentations.forEach(presentation -> {
                        contents.add(new McpSchema.TextContent(presentation.toString()));
                    });
                    return new McpSchema.CallToolResult(contents, false);
                }
        );
        return syncToolSpecification;
    }
}