package com.example.springaidemo.Agents;

import com.example.springaidemo.dto.EntityMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepositoryAgent {

    private final ChatClient.Builder builder;

    public String generate(EntityMetadata metadata) {

        ChatClient chatClient = builder.build();

        String entity = metadata.getEntity();

        String prompt = """
                Generate ONLY Spring Data JPA repository.

                STRICT RULES:
                1. No markdown.
                2. No explanation.
                3. Generate valid Java only.
                4. Use package:
                com.example.generated.repository

                Entity:
                %s
                """.formatted(entity);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}