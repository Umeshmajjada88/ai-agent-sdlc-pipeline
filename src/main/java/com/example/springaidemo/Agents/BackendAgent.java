
package com.example.springaidemo.Agents;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackendAgent {

    private final ChatClient.Builder builder;

    public String generateCrudCode(String requirement) {

        ChatClient chatClient = builder.build();

        String prompt = """
                You are an expert Spring Boot backend developer.

                Generate COMPLETE production-ready CRUD code.

                STRICT RULES:
                1. Generate valid Java code only.
                2. No markdown.
                3. No explanations.
                4. Include imports.
                5. Use Spring Boot.
                6. Use Spring Data JPA.
                7. Use Lombok.
                8. Use REST APIs.
                9. Use H2 database.
                10. Generate:
                    - Entity
                    - Repository
                    - Service
                    - Controller

                Requirement:
                %s
                """.formatted(requirement);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
