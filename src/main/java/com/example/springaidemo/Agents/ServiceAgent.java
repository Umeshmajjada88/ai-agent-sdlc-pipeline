package com.example.springaidemo.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ServiceAgent {

    private final ChatClient chatClient;

    public ServiceAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateService(
            String requirement,
            String entityCode,
            String repositoryCode) {

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

                REQUIRED IMPORTS:
                import com.example.springaidemo.entity.Student;
                import com.example.springaidemo.repository.StudentRepository;

                import org.springframework.stereotype.Service;

                import java.util.List;
                import java.util.Optional;

                ENTITY CODE:
                %s

                REPOSITORY CODE:
                %s

                Requirement:
                %s
                """.formatted(
                entityCode,
                repositoryCode,
                requirement);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}