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
                   
You are a senior software architect.

Analyze the requirement and generate structured metadata
for enterprise-grade Spring Boot code generation.

STRICT RULES:
1. Return ONLY valid JSON
2. No markdown
3. No explanations
4. No extra text
5. Infer business logic intelligently
6. Generate realistic service operations
7. Detect validations and constraints
8. Detect workflow/state management
9. Detect searchable fields
10. Detect unique fields

REQUIRED JSON FORMAT:

{
  "projectType": "Spring Boot CRUD Application",

  "entityName": "Employee",

  "fields": [
    {
      "name": "id",
      "type": "Long",
      "required": true,
      "unique": true,
      "nullable": false,
      "searchable": false
    }
  ],

  "crudOperations": [
    "Create",
    "Read",
    "Update",
    "Delete"
  ],

  "businessRules": [
    {
      "name": "Unique Email",
      "description": "Employee email must be unique"
    }
  ],

  "validations": [
    {
      "field": "email",
      "rules": [
        "NOT_NULL",
        "VALID_EMAIL",
        "UNIQUE"
      ]
    }
  ],

  "serviceMethods": [
    {
      "name": "createEmployee",
      "purpose": "Create employee after validation"
    },
    {
      "name": "updateEmployeeDepartment",
      "purpose": "Update employee department"
    }
  ],

  "repositoryMethods": [
    "findByEmail",
    "existsByEmail",
    "findByDepartment"
  ],

  "workflowStates": [
    "ACTIVE",
    "INACTIVE"
  ],

  "exceptionScenarios": [
    "Employee not found",
    "Duplicate email"
  ]
}

BUSINESS INFERENCE RULES:

EMPLOYEE DOMAIN:
- email should be unique
- email requires validation
- add ACTIVE/INACTIVE status
- generate findByDepartment
- generate existsByEmail
- generate findByEmail
- validate before update

USER DOMAIN:
- password hashing
- login support
- email uniqueness
- account locking

PRODUCT DOMAIN:
- stock validation
- quantity management
- low inventory handling

ORDER DOMAIN:
- total calculation
- order status transitions
- inventory checks

BOOKING DOMAIN:
- date validation
- overlap checks
- availability validation

GENERATE:
- realistic service methods
- repository query methods
- validation requirements
- exception scenarios
- business rules

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