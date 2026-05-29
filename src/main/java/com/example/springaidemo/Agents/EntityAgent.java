package com.example.springaidemo.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.springaidemo.DTO.RequirementDTO;
import com.example.springaidemo.utils.AIResponseCleaner;

@Service
public class EntityAgent {

    private final ChatClient chatClient;

    public EntityAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateEntity(RequirementDTO requirement) {

        String prompt = """
                You are a senior Spring Boot developer. Generate a production-grade JPA entity. 
                STRICT RULES: 
                1. Generate ONLY Java code 
                2. No markdown 
                3. No explanations
                 4. Use Lombok 
                 5. Use JPA annotations 
                 6. Add validation annotations 
                 7. Add column constraints 
                 ENTITY REQUIREMENTS:
                  - Use @Entity
                  - Use @Table 
                  - Use @Id 
                  - Use @GeneratedValue 
                  - Use @Column 
                  - Use Lombok annotations 
                  VALIDATION RULES: 
                  - NOT_NULL -> @NotNull 
                  - VALID_EMAIL -> @Email 
                  - required=true -> @NotBlank 
                  for String - unique=true -> @Column(unique = true) 
                  - nullable=false -> @Column(nullable = false) 
                  FIELDS: %s 
                  VALIDATIONS: %s 
                  ENTITY NAME: %s 
                  """.formatted(requirement.getFields(), requirement.getValidations(), requirement.getEntityName());

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return AIResponseCleaner.clean(response);
    }
}