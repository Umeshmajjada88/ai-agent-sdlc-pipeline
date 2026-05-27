package com.example.springaidemo.Agents;

import com.example.springaidemo.dto.ApiContract;
import com.example.springaidemo.dto.EntityMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ControllerAgent {

   private final ChatClient.Builder builder;

   public String generate(EntityMetadata metadata, ApiContract contract) {

      ChatClient chatClient = builder.build();

      String prompt = """
            Generate a COMPLETE Spring Boot REST Controller.

            STRICT RULES:

            1. Generate ONLY Java code.
            2. No markdown.
            3. No explanations.
            4. Use package:
               com.example.generated.controller
            5. Use ResponseEntity.
            6. Use constructor injection.
            7. Use @RestController.
            8. Use @RequestMapping.
            9.import entity at top.

            ENTITY:
            %s

            SERVICE METHOD CONTRACT (MUST USE EXACTLY):

            createMethod = %s
            getAllMethod = %s
            getByIdMethod = %s
            updateMethod = %s
            deleteMethod = %s

            Controller MUST call ONLY these service methods.
            DO NOT invent any other service methods.

            Example:

            service.%s(...)
            service.%s()
            service.%s(id)
            service.%s(id, entity)
            service.%s(id)

            Generate endpoints:

            POST   /
            GET    /
            GET    /{id}
            PUT    /{id}
            DELETE /{id}

            Return COMPLETE compilable Java class.

            Metadata:
            %s
            """
            .formatted(
                  metadata.getEntity(),

                  contract.getCreateMethod(),
                  contract.getGetAllMethod(),
                  contract.getGetByIdMethod(),
                  contract.getUpdateMethod(),
                  contract.getDeleteMethod(),

                  contract.getCreateMethod(),
                  contract.getGetAllMethod(),
                  contract.getGetByIdMethod(),
                  contract.getUpdateMethod(),
                  contract.getDeleteMethod(),

                  metadata);

      return chatClient.prompt()
            .user(prompt)
            .call()
            .content();
   }
}