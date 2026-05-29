package com.example.springaidemo.Agents;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequirementAgent {

    private final ChatClient.Builder builder;

    public String analyze(String requirement) {

        ChatClient chatClient = builder.build();

        String prompt = """
                You are a software architect.

                Return ONLY valid JSON.

                STRICT RULES:

                1. No markdown.
                2. No explanations.
                3. No comments.
                4. No Java code.
                5. Return ONLY JSON.
                6. Use only contructor Injection dont use autowired.
                &. Import List and Optional and every File if needed.


                Return format:

                {
                  "entity":"Product",
                  "fields":[
                    {
                      "name":"id",
                      "type":"Long"
                    }
                  ],
                  "apis":[
                    {
                      "name":"createProduct",
                      "method":"POST",
                      "path":"/products",
                      "logic":"Create product"
                    }
                  ]
                }

                Requirement:
                %s
                """.formatted(requirement);
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}