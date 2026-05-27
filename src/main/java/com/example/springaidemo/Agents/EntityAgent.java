package com.example.springaidemo.Agents;

import com.example.springaidemo.dto.EntityMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityAgent {

    private final ChatClient.Builder builder;

    public String generate(EntityMetadata metadata) {

        ChatClient chatClient = builder.build();

        String prompt = """
                                You are an expert Spring Boot JPA Entity generator.

                            STRICT RULES:

                1. Generate ONLY Java code.
                2. No markdown.
                3. No explanation.
                4. Use Spring Boot 3.
                5. Use ONLY jakarta.persistence imports.
                6. NEVER use javax.persistence.
                7. Use package:
                com.example.generated.entity
                8. Use Lombok.

                                com.example.generated.entity

                                Generate entity class for:

                                %s
                                """.formatted(metadata);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}