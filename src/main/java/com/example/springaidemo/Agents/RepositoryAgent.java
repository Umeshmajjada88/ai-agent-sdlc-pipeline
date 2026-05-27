package com.example.springaidemo.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.springaidemo.utils.AIResponseCleaner;

@Service
public class RepositoryAgent {

    private final ChatClient chatClient;

    public RepositoryAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateRepository(
            String requirement,
            String entityCode,
            String entityName) {

        String prompt = """
                You are an expert Spring Boot developer.

                STRICT RULES:
                1. Generate ONLY raw Java code
                2. No explanations
                3. No markdown
                4. Output must compile
                5. Use provided imports exactly
                6. Do NOT regenerate imports

                REQUIRED PACKAGE:
                package com.example.springaidemo.repository;

                REQUIRED IMPORTS:
                import com.example.springaidemo.entity.%s;

                import org.springframework.data.jpa.repository.JpaRepository;
                import org.springframework.stereotype.Repository;

                ENTITY CODE:
                %s

                Requirement:
                %s
                """.formatted(
                entityName,
                entityCode,
                requirement);

        String response = chatClient.prompt()
        .user(prompt)
        .call()
        .content();

        return AIResponseCleaner.clean(response);
    }
}