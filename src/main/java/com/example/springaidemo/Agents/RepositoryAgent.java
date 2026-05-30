package com.example.springaidemo.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.springaidemo.DTO.RequirementDTO;
import com.example.springaidemo.utils.AIResponseCleaner;

@Service
public class RepositoryAgent {

    private final ChatClient chatClient;

    public RepositoryAgent(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateRepository(
            RequirementDTO requirement,
            String entityCode,
            String entityName) {

            String prompt = """
                You are an expert Spring Boot developer.

                STRICT RULES:
                1. Generate ONLY raw Java code
                2. No explanations
                3. No markdown
                4. Output must compile
                5. Use provided imports exactly
                6. Do NOT regenerate imports
                7.Extend JpaRepository<EntityName, Long>
                8. NEVER redeclare existsById, findById, save, deleteById — these are inherited
                9. Custom methods only: findByEmail, findByDepartment, etc.
                10. All custom methods return Optional<T> or List<T>, never Boolean wrapper type
                11. No @Override annotations

                REPOSITORY REQUIREMENTS:
                 - Extend JpaRepository
                 - Use Optional where appropriate
                 - Generate derived query methods
                 - Generate existsBy methods
                 - Generate finder methods

                REQUIRED PACKAGE:
                package com.example.springaidemo.repository;

                REQUIRED IMPORTS:
                import com.example.springaidemo.entity.%s;

                import org.springframework.data.jpa.repository.JpaRepository;
                import org.springframework.stereotype.Repository;

                ENTITY CODE:
                %s

                Requirement:
                %s
                """.formatted(
                entityName,
                entityCode,
                requirement);

        // String prompt = """
        //         You are a senior Spring Data JPA developer.

        //         Generate a production-grade repository interface.

        //         STRICT RULES:
        //         1. Generate ONLY Java code
        //         2. No markdown
        //         3. No explanations
        //         4. Extend JpaRepository
        //         5. Generate custom query methods

        //         REPOSITORY REQUIREMENTS:
        //         - Extend JpaRepository
        //         - Use Optional where appropriate
        //         - Generate derived query methods
        //         - Generate existsBy methods
        //         - Generate finder methods


        //         REQUIRED PACKAGE:
        //         package com.example.springaidemo.repository;

        //         REQUIRED IMPORTS:
        //         import com.example.springaidemo.entity.%s;

        //         import org.springframework.data.jpa.repository.JpaRepository;
        //         import org.springframework.stereotype.Repository;

        //         CUSTOM METHODS:
        //         %s

        //         ENTITY NAME:
        //         %s

        //         ENTITY CODE:
        //         %s
        //         """.formatted(entityName,
        //         requirement.getRepositoryMethods(),
        //         entityName,
        //         entityCode);

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return AIResponseCleaner.clean(response);
    }
}