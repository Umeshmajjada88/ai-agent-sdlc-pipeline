package com.example.springaidemo.Agents;

import com.example.springaidemo.dto.ApiContract;
import com.example.springaidemo.dto.EntityMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceAgent {

    private final ChatClient.Builder builder;

    public String generate(EntityMetadata metadata,  ApiContract contract) {

        ChatClient chatClient = builder.build();

      String prompt = """
Generate a COMPLETE Spring Boot Service class.
MUST RULES :

1. Use constructor injection only.
2. Never use field injection.
3. Use @RequiredArgsConstructor.
4. Use final repository.
5. update<Entity>() must first load existing entity.
6. delete<Entity>() must return boolean.
7. Return typed methods only.

STRICT RULES:

1. Generate ONLY Java code.
2. No markdown.
3. No explanations.
4. Use constructor injection.
5. Use @Service.
6. Use package:
com.example.generated.service


7. Entity name:
%s

8. Repository name:
   %sRepository

9. Method Contract:

   createMethod = %s
   getAllMethod = %s
   getByIdMethod = %s
   updateMethod = %s
   deleteMethod = %s

10. Generate methods EXACTLY with these names.
11. DO NOT invent method names.
12. DO NOT generate:
    - save()
    - findAll()
    - findById()
    - deleteById()

13. Use repository methods internally.

14. Generate:

   public %s createMethod(%s entity)

   public List<%s> getAllMethod()

   public Optional<%s> getByIdMethod(Long id)

   public Optional<%s> updateMethod(Long id, %s entity)

   public boolean deleteMethod(Long id)

15. Return COMPLETE compilable Java class.

Metadata:
%s
"""
.formatted(
        metadata.getEntity(),
        metadata.getEntity(),

        contract.getCreateMethod(),
        contract.getGetAllMethod(),
        contract.getGetByIdMethod(),
        contract.getUpdateMethod(),
        contract.getDeleteMethod(),

        metadata.getEntity(),
        metadata.getEntity(),

        metadata.getEntity(),

        metadata.getEntity(),

        metadata.getEntity(),
        metadata.getEntity(),

        metadata
);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}