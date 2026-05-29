package com.example.springaidemo.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.springaidemo.DTO.RequirementDTO;
import com.example.springaidemo.utils.AIResponseCleaner;

@Service
public class RepositoryAgent {

    private final ChatClient chatClient;

    public RepositoryAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateRepository(
            RequirementDTO requirement,
            String entityCode,
            String entityName) {

        String prompt = """
                You are a senior Spring Data JPA developer.

                Generate a production-grade repository interface.

                STRICT RULES:
                1. Generate ONLY Java code
                2. No markdown
                3. No explanations
                4. Extend JpaRepository
                5. Generate custom query methods

                REPOSITORY REQUIREMENTS:
                - Extend JpaRepository
                - Use Optional where appropriate
                - Generate derived query methods
                - Generate existsBy methods
                - Generate finder methods

                CUSTOM METHODS:
                %s

                ENTITY NAME:
                %s

                ENTITY CODE:
                %s
                """.formatted(
                requirement.getRepositoryMethods(),
                entityName,
                entityCode);

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return AIResponseCleaner.clean(response);
    }
}