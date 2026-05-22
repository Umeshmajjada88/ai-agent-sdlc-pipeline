package com.example.springaidemo.Agents;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RequirementAgent {

    private final ChatClient chatClient;

    public RequirementAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String analyze(String input) {

        String prompt = """
        Analyze this CRUD application requirement.

        Return clearly:

        Entity Name:
        Fields:
        CRUD Operations:

        Requirement:
        %s
        """.formatted(input);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
