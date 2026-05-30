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
        You are an expert Spring Boot developer.

        STRICT RULES:
        1. Generate ONLY raw Java code
        2. No explanations, no markdown, no backticks
        3. Output must compile as-is
        4. ALWAYS include ALL imports for EVERY annotation you use
        5. Do NOT use any annotation without its import

        REQUIRED PACKAGE:
        package com.example.springaidemo.entity;

        COPY THESE IMPORTS EXACTLY AT THE TOP (include all of them every time):
        import jakarta.persistence.Entity;
        import jakarta.persistence.Table;
        import jakarta.persistence.Id;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Column;
        import jakarta.validation.constraints.NotNull;
        import jakarta.validation.constraints.Email;
        import jakarta.validation.constraints.Size;
        import jakarta.validation.constraints.NotBlank;
        import lombok.Data;
        import lombok.NoArgsConstructor;
        import lombok.AllArgsConstructor;

        ANNOTATION MAPPING RULES (use ONLY these):
        - required field     → @NotNull
        - email field        → @Email  (import is jakarta.validation.constraints.Email)
        - unique string      → @Column(unique = true)
        - non-null column    → @Column(nullable = false)
        - primary key        → @Id + @GeneratedValue(strategy = GenerationType.IDENTITY)

        FORBIDDEN:
        - @NotBlank (do not use)
        - @NonNull (do not use)
        - Any import from javax.* (use jakarta.* only)
        - Any class from outside this entity package

        Requirement:
        %s
        """.formatted(requirement);

        // String prompt = """
        //         You are a senior Spring Boot developer. Generate a production-grade JPA entity. 
        //         STRICT RULES: 
        //         1. Generate ONLY Java code 
        //         2. No markdown 
        //         3. No explanations
        //          4. Use Lombok 
        //          5. Use JPA annotations 
        //          6. Add validation annotations 
        //          7. Add column constraints 

        //          REQUIRED PACKAGE:
        //         package com.example.springaidemo.entity;

        //         REQUIRED IMPORTS:
        //         import jakarta.persistence.Entity;
        //         import jakarta.persistence.Id;
        //         import jakarta.persistence.GeneratedValue;
        //         import jakarta.persistence.GenerationType;
                
        //          ENTITY REQUIREMENTS:
        //           - Use @Entity
        //           - Use @Table 
        //           - Use @Id 
        //           - Use @GeneratedValue 
        //           - Use @Column 
        //           - Use Lombok annotations 
        //           - Use @Notnull instead of @NotBlank for all fields
        //           VALIDATION RULES: 
        //           - NOT_NULL -> @NotNull 
        //           - VALID_EMAIL -> @Email 
        //           - required=true -> @NotBlank 
        //           for String - unique=true -> @Column(unique = true) 
        //           - nullable=false -> @Column(nullable = false) 
        //           FIELDS: %s 
        //           VALIDATIONS: %s 
        //           ENTITY NAME: %s 
        //           """.formatted(requirement.getFields(), requirement.getValidations(), requirement.getEntityName());

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return AIResponseCleaner.clean(response);
    }
}