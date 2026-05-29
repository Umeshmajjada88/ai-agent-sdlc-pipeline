package com.example.springaidemo.Agents;

import com.example.springaidemo.dto.EntityMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepositoryAgent {

        private final ChatClient.Builder builder;

 public String generate(
        EntityMetadata metadata,
        String entityCode,
        String serviceCode,
        String serviceContract) {

                ChatClient chatClient = builder.build();

                String entity = metadata.getEntity();

                String prompt = """
                                                                Generate ONLY Spring Data JPA Repository.
                                                                SERVICE CODE IS THE SOURCE OF TRUTH.

                                                                Read every repository call used by the service.

                                                                Generate repository methods EXACTLY matching the service.

                                                                Do not invent repository methods.

                                                                Do not change return types.

                                                                Do not change parameter types.
                                                                                STRICT RULES:

                                                                                1. Generate ONLY Java code.
                                                                                2. No markdown.
                                                                                3. No explanations.
                                                                                4. Use package:
                                                                                com.example.generated.repository
                                                                                5. Import lists and optional at top.

                                                                                ENTITY:
                                                                                %s


                                                                                ENTITY CODE:

                                                                                %s
                                                                        ENTITY FIELD VALIDATION

Extract all fields from ENTITY CODE.

For every repository finder method:

1. Extract referenced field name.
2. Verify field exists in ENTITY CODE.
3. If field does not exist:
   do not generate that repository method.

Do not hardcode field names.

Infer dynamically from repository methods.

If field does not exist:

DO NOT generate the repository method.

Never invent entity fields.
Never invent finder methods.

                                                                                SERVICE CODE:
                                                                                   %s
                                                                                SERVICE CONTRACT:
                                                                                   %s
                                                                                SERVICE CONTRACT IS THE SOURCE OF TRUTH.

                                Never infer repository methods.

                                Generate repository methods matching
                                the contract exactly.

                                               
                                                                                IMPORTANT:

                                                                                - Analyze the service code.
                                                                                - Generate repository methods required by service.
                                                                                - Do not generate unused methods.
                                                                                - Extend JpaRepository<%s, Long>.
                                                                                - Generate custom finder methods referenced in service.
                                                                                - Import List , Optional.

                                                                                - Return COMPLETE compilable Java code.

                                                                                REPOSITORY COMPATIBILITY RULE

                                                                SERVICE CODE IS THE ONLY SOURCE OF TRUTH.

                                                                Generate repository methods EXACTLY matching
                                                                the repository calls used by the service.

                                                                Do not invent repository methods.
                                                                Do not change return types.
                                                                Do not change parameter types.


                                                                SERVICE CONTRACT IS THE ONLY SOURCE OF TRUTH.

For every method:

METHOD:
SERVICE_RETURN_TYPE:
PARAMETERS:

Generate repository methods matching the service contract.

Never infer List.
Never infer Optional.
Never infer Entity.

Use the exact type from the contract.

IMPORT RULES

If List is used:
import java.util.List;

If Optional is used:
import java.util.Optional;

Never use fully qualified names.

Every referenced type must be imported.

Before returning code verify:

all_required_imports_exist == true

                                                                               
                                             

                                                                                FINAL VALIDATION:

                                                                                1. Every service repository call must exist.
                                                                                2. No missing imports.
                                                                                3. Compiles with Spring Boot 3 and Java 17.
                                                                                """
  .formatted(
    entity,
    entityCode,
    serviceCode,
    serviceContract,
    entity
);

                return chatClient.prompt()
                                .user(prompt)
                                .call()
                                .content();
        }
}