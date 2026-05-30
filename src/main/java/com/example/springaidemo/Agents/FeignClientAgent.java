package com.example.springaidemo.Agents;

import com.example.springaidemo.DTO.RequirementDTO;
import com.example.springaidemo.utils.AIResponseCleaner;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class FeignClientAgent {

    private final ChatClient chatClient;

    public FeignClientAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateFeignClient(
            RequirementDTO requirement) {

        String entityName = requirement.getEntityName();

        String prompt = """
                You are an expert Spring Boot developer.

                STRICT RULES:
                1. Generate ONLY raw Java code
                2. No explanations
                3. No markdown
                4. Output must compile

                Generate Feign Client for:
                Payment Service communication.

                REQUIRED PACKAGE:
                package com.example.springaidemo.feign;

                REQUIRED IMPORTS:

                import com.example.springaidemo.entity.%s;
                import org.springframework.cloud.openfeign.FeignClient;
                import org.springframework.web.bind.annotation.*;

                REQUIRED CLASS NAME:
                PaymentClient

                REQUIREMENTS:
                - Use @FeignClient
                - Add POST endpoint
                - Add GET endpoint

                Entity:
                %s
                """.formatted(entityName,entityName);

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return AIResponseCleaner.clean(response);
    }
}