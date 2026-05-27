package com.example.springaidemo.Agents;

import com.example.springaidemo.DTO.RequirementDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RequirementAgent {

    private final ChatClient chatClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RequirementAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public RequirementDTO analyze(String input) {

        try {

            String prompt = """
                    Analyze this CRUD requirement.

                    STRICT RULES:
                    1. Return ONLY valid JSON
                    2. No markdown
                    3. No explanations
                    4. No extra text

                    REQUIRED JSON FORMAT:

                    {
                      "entityName": "Student",
                      "fields": [
                        {
                          "name": "id",
                          "type": "Long"
                        }
                      ],
                      "crudOperations": [
                        "Create",
                        "Read",
                        "Update",
                        "Delete"
                      ]
                    }

                    Requirement:
                    %s
                    """.formatted(input);

            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            response = response.replace("```json", "");
            response = response.replace("```", "");
            response = response.trim();

            return objectMapper.readValue(
                    response,
                    RequirementDTO.class);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse requirement JSON",
                    e);
        }
    }
}