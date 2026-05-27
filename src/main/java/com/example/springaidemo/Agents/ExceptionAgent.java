package com.example.springaidemo.Agents;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExceptionAgent {

    private final ChatClient.Builder builder;

    public String generate() {

        ChatClient chatClient = builder.build();

        String prompt = """
                Generate:

                1. ResourceNotFoundException
                2. GlobalExceptionHandler

                STRICT RULES:
                - Java code only
                - No markdown
                - No explanation
                - Use Spring Boot best practices
                """;

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}