package com.example.springaidemo.Agents;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class BackendAgent {

    private final ChatClient chatClient;

    public BackendAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateCrudCode(String requirement) {

        String prompt = """
                You are a Spring Boot Backend Developer AI.

                Generate COMPLETE CRUD application code.

                STRICTLY generate ALL of the following:

                1. Entity Class
                2. Repository Interface
                3. Service Class
                4. Controller Class

                Use:
                - Spring Boot
                - Spring Data JPA
                - H2 Database

                IMPORTANT:
                Generate complete code for each class separately.

                Requirement:
                %s
                """.formatted(requirement);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}