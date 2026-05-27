package com.example.springaidemo.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.springaidemo.DTO.RequirementDTO;
import com.example.springaidemo.utils.AIResponseCleaner;

@Service
public class ServiceAgent {

    private final ChatClient chatClient;

    public ServiceAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateService(
            RequirementDTO requirement,
            String entityCode,
            String repositoryCode,
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
                package com.example.springaidemo.service;

                Examples of required inputs based on entity class and repository class are as follows:
                REQUIRED IMPORTS:
                import com.example.springaidemo.entity.%s;
                import com.example.springaidemo.repository.%sRepository;

                import org.springframework.stereotype.Service;

                import java.util.List;
                import java.util.Optional;

                ENTITY CODE:
                %s

                REPOSITORY CODE:
                %s

                Requirement:
                %s
                """.formatted(entityName,
                        entityName,
                entityCode,
                repositoryCode,
                requirement);

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return AIResponseCleaner.clean(response);
    }
}