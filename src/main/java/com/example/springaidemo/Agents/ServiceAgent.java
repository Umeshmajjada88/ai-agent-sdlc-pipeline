package com.example.springaidemo.Agents;

import com.example.springaidemo.dto.ApiMetadata;
import com.example.springaidemo.dto.EntityMetadata;
import com.example.springaidemo.dto.ServiceGenerationResult;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceAgent {

        private final ChatClient.Builder builder;

        public ServiceGenerationResult generate(
                        EntityMetadata metadata,
                        List<ApiMetadata> apis,
                        String entityCode) {

                ChatClient chatClient = builder.build();

                StringBuilder apiDetails = new StringBuilder();

                for (ApiMetadata api : apis) {

                        String logic = api.getLogic() == null
                                        ? "No business logic provided"
                                        : api.getLogic();

                        apiDetails.append("""
                                        API NAME: %s
                                        METHOD: %s
                                        PATH: %s
                                        LOGIC: %s

                                        """
                                        .formatted(
                                                        api.getName(),
                                                        api.getMethod(),
                                                        api.getPath(),
                                                        logic));
                }

                StringBuilder requiredMethods = new StringBuilder();

                for (ApiMetadata api : apis) {

                        requiredMethods.append("- ")
                                        .append(api.getName())
                                        .append("\n");
                }
                if (apis == null || apis.isEmpty()) {
                        throw new RuntimeException("No APIs provided");
                }
                String prompt = """
                                                                                                                Generate a COMPLETE Spring Boot Service class.
                                                                                                                CRITICAL ENTITY COMPATIBILITY RULE

                                                                ENTITY CODE IS THE ONLY SOURCE OF TRUTH.

                                                                Before generating service code:

                                                                1. Read all fields from ENTITY CODE.
                                                                2. Read all constructors from ENTITY CODE.
                                                                3. Read all methods from ENTITY CODE.

                                                                FORBIDDEN:

                                                                - Referencing a field not present in ENTITY CODE.
                                                                - Calling a getter not present in ENTITY CODE.
                                                                - Calling a setter not present in ENTITY CODE.
                                                                - Calling a constructor not present in ENTITY CODE.
                                                                - Creating helper methods not present in ENTITY CODE.

                                                                Every field access must be verified against ENTITY CODE.

                                                                Every method call on the entity must be verified against ENTITY CODE.

                                                                Every constructor call must be verified against ENTITY CODE.

                                                                If a field/method/constructor does not exist in ENTITY CODE,
                                                                DO NOT generate code that uses it.

                                                                Generate code only from verified entity members.
                                                                ENTITY SAFETY CHECK

                                     API COVERAGE RULE

Generate exactly one service method per API.

Method name must exactly match API NAME.

Before returning code:

service_method_count == api_count

For every API:
service_method_name == api_name
API METHOD MATCHING RULE

For every API definition:

Extract API NAME.

The generated service method name MUST be
exactly identical to API NAME.

Do not rename methods.

Do not modify method names.

Do not singularize or pluralize names.

Do not shorten names.

Do not expand names.

Before returning code verify:

every_api_name_exists_in_service == true

Example:

API NAME:
xyz

Generated Method:
xyz

                                                                If verification fails,
                                                                regenerate the service class.

                                                                                                                ==================================================
                                                                                                                STRICT RULES
                                                                                                                ==================================================

                                                                                                                1. Generate ONLY Java code.
                                                                                                                2. No markdown.
                                                                                                                3. No explanations.
                                                                                                                4. Use @Service.
                                                                                                                5. Use constructor injection only.
                                                                                                                6. Use final repository.
                                                                                                                7. Do NOT use @Autowired.
                                                                                                                8. Package:
                                                                                                                com.example.generated.service

                                                                                                                ==================================================
                                                                                                                ENTITY
                                                                                                                ==================================================

                                                                                                                ENTITY NAME:

                                                                                                                %s

                                                                                                                ENTITY CODE:

                                                                                                                %s

                                                                                                                ==================================================
                                                                                                                API DEFINITIONS
                                                                                                                ==================================================

                                                                                                                %s

                                                                                                         ==================================================
                                                                                                CRITICAL API CONTRACT
                                                                                                ==================================================

                                                                                                API COUNT = %d

                                                                                                REQUIRED SERVICE METHODS:

                                                                                                %s

                                                                                                Every method listed above MUST exist in the generated service class.

                                                                                                The API DEFINITIONS section is the ONLY source
                                                                                                of service methods.

                                                                                                MANDATORY RULES:

                                                                                                1. Generate EXACTLY one service method per API.
                                                                                                2. Generate EXACTLY %d service methods.
                                                                                                3. Never skip an API.
                                                                                                4. Never merge APIs.
                                                                                                5. Never generate extra methods.
                                                                                                6. Method name MUST exactly match API NAME.
                                                                                                7. Every required method above MUST exist.
                                                                                                8. import Lists and optional at top

                                                                                                OUTPUT IS INVALID IF EVEN ONE METHOD IS MISSING.

                                                                                                Before returning code verify:

                                                                                                generated_method_count == api_count

                                                                                                AND

                                                                                                all_required_methods_exist == true

                                                                                                If validation fails,
                                                                                                regenerate the service class.

                                                                                                                ==================================================
                                                                                                                ENTITY RULES
                                                                                                                ==================================================

                                                                                                                ENTITY CODE IS THE SOURCE OF TRUTH.

                                                                                                                - Use ONLY fields that exist in ENTITY CODE.
                                                                                                                - Use ONLY methods that exist in ENTITY CODE.
                                                                                                                - Never invent fields.
                                                                                                                - Never invent getters.
                                                                                                                - Never invent setters.
                                                                                                                - Never invent helper methods.
                                                                                                                - Verify every field reference exists.

                                                                                                                ==================================================
                                                                                                                METHOD GENERATION RULES
                                                                                                                ==================================================

                                                                                                                For every API:

                                                                                                                SERVICE METHOD GENERATION SAFETY

                                Never generate:

                                new Entity(...)

                                unless constructor exists in ENTITY CODE.

                                Never call:

                                entity.setXxx(...)

                                unless setter exists in ENTITY CODE.

                                Never reference:

                                entity.getXxx()

                                unless getter exists in ENTITY CODE.

                                Never invent entity fields.

                                Only use members present in ENTITY CODE.

                                                                                                                - Method name must exactly match API NAME.
                                                                                                                - Derive parameters from:
                                                                                                                    * API path
                                                                                                                    * API method
                                                                                                                    * API logic
                                                                                                                    * Entity structure

                                                                                                                - Derive return type from:
                                                                                                                    * API purpose
                                                                                                                    * API logic
                                                                                                                    * API path

                                                                                                                - Use standard Spring Boot service patterns.

                                                                                                                ==================================================
                                                                                                                REPOSITORY RULES
                                                                                                                ==================================================

                                                                                                                - Use repository internally.
                                                                                                                - Generate repository calls when needed.
                                                                                                                - If custom repository methods are required,
                                                                                                                  still generate the service method.
                                                                                                                - RepositoryAgent will generate repository methods later.
                                                                                                                - Never skip a service method because a repository method
                                                                                                                  does not yet exist.

                                                                                                                - Do not assume repository method names.
                                                                                                                - Derive repository methods only from:
                                                                                                                    * API definitions
                                                                                                                    * Entity fields
                                                                                                                    * Business logic

                                                                                                                ==================================================
                                                                                                                CONTROLLER COMPATIBILITY RULES
                                                                                                                ==================================================

                                                                                                                ControllerAgent will use this service class
                                                                                                                as the source of truth.

                                                                                                                Every API listed above MUST have
                                                                                                                a corresponding service method.

                                                                                                                No controller method should ever reference
                                                                                                                a missing service method.

                                                                                                                ==================================================
                                                                                                                IMPORT RULES
                                                                                                                ==================================================

                                                                                                                - Import all required classes.
                                                                                                                - Import List only if used.
                                                                                                                - Import Optional only if used.
                                                                                                                - No unused imports.
                                                                                                                - No missing imports.

                                                                                                                ==================================================
                                                                SERVICE RETURN TYPE RULES
                                                                ==================================================

                                                                Determine service return type using these rules:


                                                           State Change APIs:

                                Choose the most appropriate return type
                                based on API purpose and implementation.

                                Do not assume boolean.
                                Do not assume Entity.
                                Do not assume Optional.

                                Derive return type from:
                                - API name
                                - API logic
                                - API path
                                - Entity structure

                                                                Calculation API:
                                                                Return calculated value type.

                                                                Custom APIs:
                                                                Choose the most appropriate type based on API purpose.

                                                                ==================================================
                                                                SERVICE CONSISTENCY RULE
                                                                ==================================================

                                                                For every API:

                                                                1. Generate exactly one service method.
                                                                2. Generate one consistent return type.
                                                                3. Repository usage must match return type.
                                                                4. Controller will use this service as source of truth.

                                                                Before returning code verify:

                                                                - every API has a method
                                                                - every method has a return type
                                                                - return type matches implementation

                                                                

                                                                                                                ==================================================
                                                                                                                FINAL VALIDATION
                                                                                                                ==================================================



                                                                                                                Verify:

                                                                                                                1. Every API has a service method.
                                                                                                                2. Service method count equals API count.
                                                                                                                3. Every referenced field exists in ENTITY CODE.
                                                                                                                4. Every referenced type is imported.
                                                                                                                5. No unresolved symbols exist.
                                                                                                                6. Code compiles with Java 17.
                                                                                                                7. Code compiles with Spring Boot 3.
                                                                                                                8. Return ONLY Java code.
                                                                                                                3. Every generated service method name exactly
   matches an API NAME.

4. No API NAME is missing from the service.




                                                                                                                REQUIRED METHODS

                                                                %s

                                                                You MUST generate every method above.

                                                                Before returning code verify:

                                                                - Every required method exists.
                                                                - Method count equals API count.

                                                                Output is invalid if even one method is missing.


                                                                                                                Metadata:

                                                                                                                %s
                                                                                                                """
                                .formatted(
                                                metadata.getEntity(), // ENTITY NAME
                                                entityCode, // ENTITY CODE
                                                apiDetails, // API DEFINITIONS
                                                apis.size(), // API COUNT
                                                requiredMethods, // REQUIRED SERVICE METHODS
                                                apis.size(), // EXACT METHOD COUNT
                                                requiredMethods, // REQUIRED METHODS SECTION AT BOTTOM
                                                metadata // METADATA
                                );

                System.out.println("===== SERVICE PROMPT =====");
                System.out.println(prompt);
                System.out.println("SERVICE API COUNT = " + apis.size());

                String serviceCode = chatClient.prompt()
                                .user(prompt)
                                .call()
                                .content();
                String contractPrompt = """
                                                                Analyze the following generated service code.

                                                                For EVERY public service method return:

                                                                METHOD:
                                                                SERVICE_RETURN_TYPE:
                                                                PARAMETERS:

                                                                Example:

                                                                METHOD: getPatientByEmail
                                                                SERVICE_RETURN_TYPE: Optional<Patient>
                                                                PARAMETERS: String email

                                                                METHOD: getPatientsByBloodGroup
                                                                SERVICE_RETURN_TYPE: List<Patient>
                                                                PARAMETERS: String bloodGroup

                                                                Return ONLY plain text.

                                                                SERVICE CODE:



                                                                %s


                                                                """.formatted(serviceCode);

                String serviceContract = chatClient.prompt()
                                .user(contractPrompt)
                                .call()
                                .content();

                return new ServiceGenerationResult(
                                serviceCode,
                                serviceContract);
        }
}