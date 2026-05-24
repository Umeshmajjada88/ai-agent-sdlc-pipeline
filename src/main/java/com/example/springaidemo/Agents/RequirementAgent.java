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

                Return ONLY raw JSON.

DO NOT generate markdown.
DO NOT use ```json.
DO NOT explain anything.


                Example:

                {
                "entity":"Employee",
                "fields":[
                    {"name":"id","type":"Long"},
                    {"name":"name","type":"String"},
                    {"name":"salary","type":"Double"}
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