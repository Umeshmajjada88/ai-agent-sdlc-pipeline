package com.example.springaidemo.Agents;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackendAgent {

    private final ChatClient.Builder builder;

    public String generateCrudCode(String requirement) {

        ChatClient chatClient = builder.build();

        String prompt = """
                You are an elite Senior Spring Boot Backend Developer AI.

                Your responsibility is to generate COMPLETE enterprise-grade
                Spring Boot CRUD application code.

                STRICT GLOBAL RULES:

                1. Generate ONLY valid production-ready code.
                2. DO NOT generate markdown.
                
                3. DO NOT generate explanations.
                4. DO NOT generate comments.
                5. DO NOT generate pseudo code.
                6. DO NOT skip imports.
                7. Generate compilable code only.
                8. Use Java 17.
                9. Use Spring Boot 3.
                10. Use Maven project structure.
                11. Use layered architecture.
                12. Use constructor injection only.
                13. Use clean coding standards.
                14. Use proper naming conventions.
                15. Generate complete code class-by-class.
                16. Use package:
                    com.example.generated
                17. Use H2 Database.
                18. Use Spring Data JPA.
                19. Use Lombok.
                20. Use REST APIs.
                21. Generate fully working APIs.
                22. Add complete CRUD operations.
                23. Add proper exception handling.
                24. Add validation annotations.
                25. Add ResponseEntity responses.
                26. Add service layer validations.
                27. Add @Transactional where required.
                28. Avoid field injection.
                29. Avoid deprecated APIs.
                30. Generate application.properties.
                31. Generate pom.xml.

                STRICTLY GENERATE THESE FILES:

                1. pom.xml
                2. application.properties
                3. MainApplication.java
                4. Entity Class
                5. Repository Interface
                6. Service Class
                7. Controller Class
                8. DTO Classes
                9. Exception Classes
                10. GlobalExceptionHandler

                ENTITY RULES:

                - Use @Entity
                - Use @Table
                - Use @Id
                - Use @GeneratedValue(strategy = GenerationType.IDENTITY)
                - Use Lombok annotations
                - Add validation annotations

                DATATYPE RULES:

                Use:
                - id -> Long
                - name -> String
                - description -> String
                - age -> Integer
                - salary -> Double
                - amount -> Double
                - price -> Double
                - quantity -> Integer
                - stock -> Integer
                - active -> Boolean
                - enabled -> Boolean
                - email -> String
                - phone -> String
                - address -> String
                - createdDate -> LocalDate
                - createdAt -> LocalDateTime
                - updatedAt -> LocalDateTime

                CONTROLLER RULES:

                Generate these endpoints:
                - POST
                - GET ALL
                - GET BY ID
                - PUT
                - DELETE

                CONTROLLER REQUIREMENTS:

                - Use @RestController
                - Use @RequestMapping
                - Use ResponseEntity
                - Add proper status codes
                - Add exception handling

                SERVICE RULES:

                - Use @Service
                - Use @Transactional
                - Use repository layer
                - Add validation checks
                - Throw custom exceptions

                REPOSITORY RULES:

                - Extend JpaRepository

                EXCEPTION HANDLING RULES:

                Generate:
                - ResourceNotFoundException
                - GlobalExceptionHandler

                VALIDATION RULES:

                Add:
                - @NotNull
                - @NotBlank
                - @Size
                - @Email
                wherever applicable.

                APPLICATION.PROPERTIES RULES:

                Configure:
                - server.port=8000
                - H2 database
                - Hibernate ddl-auto
                - show-sql
                - h2 console

                POM.XML RULES:

                Add dependencies:
                - spring-boot-starter-web
                - spring-boot-starter-data-jpa
                - spring-boot-starter-validation
                - h2
                - lombok

                OUTPUT FORMAT STRICTLY:

                FILE: pom.xml
                <FULL CONTENT>

                FILE: application.properties
                <FULL CONTENT>

                FILE: MainApplication.java
                <FULL CONTENT>

                FILE: Employee.java
                <FULL CONTENT>

                FILE: EmployeeRepository.java
                <FULL CONTENT>

                FILE: EmployeeRequestDto.java
                <FULL CONTENT>

                FILE: EmployeeResponseDto.java
                <FULL CONTENT>

                FILE: EmployeeService.java
                <FULL CONTENT>

                FILE: EmployeeController.java
                <FULL CONTENT>

                FILE: ResourceNotFoundException.java
                <FULL CONTENT>

                FILE: GlobalExceptionHandler.java
                <FULL CONTENT>

                IMPORTANT:
                Every file must be complete and compilable.

                Requirement:
                %s
                """.formatted(requirement);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}