package com.example.springaidemo.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class EntityAgent {

    private final ChatClient chatClient;

    public EntityAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateEntity(String requirement) {

        String prompt = """
                You are an expert Spring Boot developer.

                STRICT RULES:
                1. Generate ONLY raw Java code
                2. No explanations
                3. No markdown
                4. Output must compile
                5. Use the provided package and imports exactly
                6. Do NOT regenerate imports

                REQUIRED PACKAGE:
                package com.example.springaidemo.entity;

                REQUIRED IMPORTS:
                import jakarta.persistence.Entity;
                import jakarta.persistence.Id;
                import jakarta.persistence.GeneratedValue;
                import jakarta.persistence.GenerationType;

                import lombok.Data;

                Requirement:
                %s
                """.formatted(requirement);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}