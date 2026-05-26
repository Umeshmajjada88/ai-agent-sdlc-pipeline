package com.example.springaidemo.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.springaidemo.utils.AIResponseCleaner;

@Service
public class ControllerAgent {

    private final ChatClient chatClient;

    public ControllerAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateController(
            String requirement,
            String entityCode,
            String serviceCode) {

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
                package com.example.springaidemo.controller;

                REQUIRED IMPORTS:
                import com.example.springaidemo.entity.Student;
                import com.example.springaidemo.service.StudentService;

                import org.springframework.http.ResponseEntity;
                import org.springframework.web.bind.annotation.*;

                import java.util.List;
                import java.util.Optional;

                ENTITY CODE:
                %s

                SERVICE CODE:
                %s

                Requirement:
                %s
                """.formatted(
                entityCode,
                serviceCode,
                requirement);

        String response = chatClient.prompt()
        .user(prompt)
        .call()
        .content();

        return AIResponseCleaner.clean(response);
    }
}