package com.example.springaidemo.Agents;

import com.example.springaidemo.dto.ApiMetadata;
import com.example.springaidemo.dto.EntityMetadata;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ControllerAgent {

        private final ChatClient.Builder builder;

        public String generate(
                        EntityMetadata metadata,
                        String entityCode,
                        List<ApiMetadata> apis,
                        String serviceCode,
                        String serviceContract) {

                ChatClient chatClient = builder.build();

                StringBuilder apiDetails = new StringBuilder();

                for (ApiMetadata api : apis) {

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
                                                        api.getLogic()));
                }
                String prompt = """



                                                                                                                                                                                                                                                                    Generate a COMPLETE Spring Boot REST Controller.



                                                                                                                                                                                                                                                                    SERVICE CODE IS SOURCE OF TRUTH.

                                                                                                                                                                                                        Do not infer return types.

                                                                                                                                                                                                        Extract service return type exactly.

                                                                                                                                                                                                        Mapping:

                                                                                                                                                                                                        Service Return Type          Controller Return Type

                                                                                                                                                                                                        Entity                       ResponseEntity<Entity>

                                                                                                                                                                                                        List<Entity>                 ResponseEntity<List<Entity>>

                                                                                                                                                                                                        Optional<Entity>             ResponseEntity<Entity>

                                                                                                                                                                                                        boolean                      ResponseEntity<Void>

                                                                                                                                                                                                        void                         ResponseEntity<Void>

                                                                                                                                                                                                        Use this mapping exactly.

                                                                                                                                                                                                                                STEP 1:
                                                                                                                                                                                                                                Read every public service method.

                                                                                                                                                                                                                                STEP 2:
                                                                                                                                                                                                                                Extract for each method:
                                                                                                                                                                                                                                - method name
                                                                                                                                                                                                                                - parameter list
                                                                                                                                                                                                                                - return type

                                                                                                                                                                                                                                STEP 3:
                                                                                                                                                                                                                                Generate controller using EXACTLY the same:

                                                                                                                                                                                                                                - method name
                                                                                                                                                                                                                                - parameter types
                                                                                                                                                                                                                                - return type behavior

                                                                                                                                                                                                                                NEVER infer return types from API names.

                                                                                                                                                                                                                                NEVER infer return types from business logic.

                                                                                                                                                                                                                                USE ONLY THE SERVICE CODE.

                                                                                                                                                                                                                                If service contains:

                                                                                                                                                                                                                                public Optional<Entity> method(...)

                                                                                                                                                                                                                                then controller must use Optional handling.

                                                                                                                                                                                                                                If service contains:

                                                                                                                                                                                                                                public List<Entity> method(...)

                                                                                                                                                                                                                                then controller must return:

                                                                                                                                                                                                                                ResponseEntity<List<Entity>>

                                                                                                                                                                                                                                and never use map().

                                                                                                                                                                                                                                If service contains:

                                                                                                                                                                                                                                public Entity method(...)

                                                                                                                                                                                                                                then controller must return:

                                                                                                                                                                                                                                ResponseEntity<Entity>

                                                                                                                                                                                                                                If service contains:

                                                                                                                                                                                                                                public boolean method(...)

                                                                                                                                                                                                                                then controller must return:

                                                                                                                                                                                                                                ResponseEntity<Void>

                                                                                                                                                                                                                                using boolean success handling.

                                                                                                                                                                                                                                If service contains:

                                                                                                                                                                                                                                public void method(...)

                                                                                                                                                                                                                                then controller must return:

                                                                                                                                                                                                                                ResponseEntity<Void>

                                                                                                                                                                                                                                and call the service directly.

                                                                                                                                                                                                                                                                    SERVICE CODE IS THE SOURCE OF TRUTH.

                                                                                                                                                                                                                                                                    SERVICE CODE:
                                                                                                                                                                                                                                                                    %s

                                                                                                                                                                                                                                                                    SERVICE CONTRACT:
                                                                                                                                                                                                                                                                    %s


                                                                                                                                     SERVICE CONTRACT IS SOURCE OF TRUTH.

                                                                                                Do not infer:

                                                                                                - method names
                                                                                                - return types
                                                                                                - parameters

                                                                                                Use the contract exactly.

                                                                                                                                                                                                                                                                    IMPORTANT RULES:

                                                                                                                                                                                                                                                                    1. Read the service code carefully.
                                                                                                                                                                                                                                                                    2. Extract service method signatures.
                                                                                                                                                                                                                                                                    3. Use EXACT service method names.
                                                                                                                                                                                                                                                                    4. Use EXACT service return types.
                                                                                                                                                                                                                                                                    5. Use EXACT service parameters.
                                                                                                                                                                                                                                                                    6. Do not rename methods.
                                                                                                                                                                                                                                                                    7. Do not invent methods.
                                                                                                                                                                                                                                                                    8. Do not change return types.
                                                                                                                                                                                                                                                                    9. Do not change parameter types.
                                                                                                                                                                                                                                                                    10. Generate controller code that compiles with this service.
                                                                                                                                                                                                                                                                    11. Use only contructor Injection dont use autowired.



                                                                                                                                                                                                                                                                    ENTITY:

                                                                                                                                                                                                                                                                    %s

                                                                                                                                                                                                                                                                    ENTITY CODE:

                                                                                                %s

                                                                                                ENTITY FIELD VALIDATION

                                                                                                Extract all fields from ENTITY CODE.

                                                                                                Before generating endpoints:

                                                                                                - Do not generate request parameters using fields that do not exist.
                                                                                                - Do not generate path variables based on non-existent fields.
                                                                                                - Do not assume entity properties exist.
                                                                                                - Use only fields present in ENTITY CODE.

                                                                                                                                                                                                                                                                    API DEFINITIONS:

                                                                                                                                                                                                                                                                    %s

                                                                                                                                                                                                                                                                    CONTROLLER RULES:

                                                                                                                                                                                                                                                                    1. Generate exactly one endpoint per API.
                                                                                                                                                                                                                                                                  API METHOD MATCHING RULE

                                                                                                For every API definition:

                                                                                                Extract:
                                                                                                - API NAME
                                                                                                - METHOD
                                                                                                - PATH

                                                                                                The generated controller method name MUST be exactly
                                                                                                equal to the API NAME.

                                                                                                Do not rename methods.

                                                                                                Do not modify method names.

                                                                                                Do not singularize, pluralize, abbreviate,
                                                                                                or expand method names.

                                                                                                Examples only:

                                                                                                API NAME: xyz
                                                                                                Controller Method: xyz

                                                                                                API NAME: abc
                                                                                                Controller Method: abc

                                                                                                Before returning code:

                                                                                                For every API:
                                                                                                generated_method_name == api_name

                                                                                                If any API method is missing or renamed:
                                                                                                REGENERATE before returning code.
                                                                                                                                                                                                                                                                    3. Endpoint path MUST match PATH.
                                                                                                                                                                                                                                                                    4. HTTP method MUST match METHOD.
                                                                                                                                                                                                                                                                    5. Call service methods with the same name.
                                                                                                                                                                                                                                                                    6. Use constructor injection only.
                                                                                                                                                                                                                                                                    7. Use @RestController.
                                                                                                                                                                                                                                                                    8. Use @RequestMapping.
                                                                                                                                                                                                                                                                    9. Use ResponseEntity.
                                                                                                                                                                                                                                                                    10. Do not generate extra endpoints.

                                                                                                                                                                                                                                                                    IMPORT RULES:

                                                                                                                                                                                                                                                                    - Import all required classes.
                                                                                                                                                                                                                                                                    - Import ResponseEntity.
                                                                                                                                                                                                                                                                    - Import Spring annotations.
                                                                                                                                                                                                                                                                    - Import Entity class.
                                                                                                                                                                                                                                                                    - Import Service class.
                                                                                                                                                                                                                                                                    - Import List if used.
                                                                                                                                                                                                                                                                    - Import Optional if used.
                                                                                                                                                                                                                                                                    - No missing imports.
                                                                                                                                                                                                                                                                  SERVICE CODE IS SOURCE OF TRUTH.

                                                                                                                                                                                                                                                                  CONTROLLER RETURN TYPE MAPPING

                                                                                                                                                                                Service Return Type                 Controller Return Type

                                                                                                                                                                                Entity                              ResponseEntity<Entity>

                                                                                                                                                                                List<Entity>                        ResponseEntity<List<Entity>>

                                                                                                                                                                                Optional<Entity>                    ResponseEntity<Entity>

                                                                                                                                                                                boolean                             ResponseEntity<Void>

                                                                                                                                                                                void                                ResponseEntity<Void>

                                                                                                                                                                                MANDATORY RULES

                                                                                                                                                                                If service returns Optional<T>

                                                                                                                                                                                Controller signature MUST be:

                                                                                                                                                                                ResponseEntity<T>

                                                                                                                                                                                Example:

                                                                                                                                                                                Service:

                                                                                                                                                                                public Optional<User> findUser(String email)

                                                                                                                                                                                Controller:

                                                                                                                                                                                public ResponseEntity<User> findUser(String email) {

                                                                                                                                                                                    return userService.findUser(email)
                                                                                                                                                                                            .map(ResponseEntity::ok)
                                                                                                                                                                                            .orElse(ResponseEntity.notFound().build());
                                                                                                                                                                                }

                                                                                                                                                                                NEVER GENERATE:

                                                                                                                                                                                ResponseEntity<Optional<T>>

                                                                                                                                                                                Only Optional uses:

                                                                                                                                                                                .map(ResponseEntity::ok)

                                                                                                                                                                                List<T> must use:

                                                                                                                                                                                ResponseEntity.ok(service.method(...))

                                                                                                                                                                                Entity must use:

                                                                                                                                                                                ResponseEntity.ok(service.method(...))

                                                                                                                                                                                boolean must use:

                                                                                                                                                                                service.method(...)
                                                                                                                                                                                        ? ResponseEntity.noContent().build()
                                                                                                                                                                                        : ResponseEntity.notFound().build()

                                                                                                                                                                                void must use:

                                                                                                                                                                                service.method(...);
                                                                                                                                                                                return ResponseEntity.noContent().build();


                                                                                                                                                                                METHOD SIGNATURE VALIDATION

                                                                                                                                                                                For every service method:

                                                                                                                                                                                1. Extract method name.
                                                                                                                                                                                2. Extract parameter list.
                                                                                                                                                                                3. Extract return type.

                                                                                                                                                                                Generate controller using exactly:

                                                                                                                                                                                - same method name
                                                                                                                                                                                - same parameters
                                                                                                                                                                                - correct ResponseEntity mapping

                                                                                                                                                                                Never infer return types from API name.
                                                                                                                                                                                Never infer return types from business logic.

                                                                                                                                                                                Use SERVICE CODE only.



                                                                                                                                                                                                                                                                    FINAL VALIDATION:

                                                                                                                                                                                                                                                                    Before generating code verify:

                                                                                                                                                                                                                                                                    1. Every referenced type is imported.
                                                                                                                                                                                                                                                                    2. Every service method exists in SERVICE CODE.
                                                                                                                                                                                                                                                                    3. Return types match SERVICE CODE.
                                                                                                                                                                                                                                                                    4. Parameters match SERVICE CODE.
                                                                                                                                                                                                                                                                    5. No unresolved symbols exist.
                                                                                                                                                                                                                                                                    6. Code compiles with Java 17 and Spring Boot 3.
                                                                                                                                                                                                                                                                    7. import Lists and optional at top


                                                                                                                                                                                                                                                                    RETURN TYPE HANDLING RULES

                                                                                                                                                                                                                                                        If service returns Optional<T>:

                                                                                                                                                                                                                                                        return service.method(...)
                                                                                                                                                                                                                                                                .map(ResponseEntity::ok)
                                                                                                                                                                                                                                                                .orElse(ResponseEntity.notFound().build());

                                                                                                                                                                                                                                                        If service returns List<T>:

                                                                                                                                                                                                                                                        return ResponseEntity.ok(service.method(...));

                                                                                                                                                                                                                                                        If service returns T:

                                                                                                                                                                                                                                                        return ResponseEntity.ok(service.method(...));

                                                                                                                                                                                                                                                        If service returns boolean:

                                                                                                                                                                                                                                                        return service.method(...)
                                                                                                                                                                                                                                                               ? ResponseEntity.noContent().build()
                                                                                                                                                                                                                                                               : ResponseEntity.notFound().build();

                                                                                                                                                                                                                                                        Never call map() on List.
                                                                                                                                                                                                                                                        Never call map() on Entity.
                                                                                                                                                                                                                                                        Only call map() on Optional.


                                                                                                                                                                                                                                                                    CRITICAL INSTRUCTION:

                                                                                                                                                                                                                                                        The APIs section is the ONLY source of methods to generate.

                                                                                                                                                                                                                                                        Count the APIs provided.

                                                                                                                                                                                                                                                        Generate EXACTLY one method per API.

                                                                                                                                                                                                                                                        If 8 APIs are provided:
                                                                                                                                                                                                                                                        - Generate exactly 8 methods.
                                                                                                                                                                                                                                                        - No more.
                                                                                                                                                                                                                                                        - No less.

                                                                                                                                                                                                                                                        Do NOT generate CRUD automatically.

                                                                                                                                                                                                                                                        Do NOT skip APIs.

                                                                                                                                                                                                                                                        Before returning code, verify:
                                                                                                                                                                                                                                                        generated_method_count == api_count

                                                                                                                                                                                                                                                                    Return ONLY Java code.
                                                                                                                                                                                                                                                                    API COVERAGE VALIDATION

                                Count all APIs provided.

                                For each API:

                                1. Verify a controller method exists.
                                2. Verify method name exactly equals API NAME.
                                3. Verify endpoint path equals API PATH.
                                4. Verify HTTP annotation equals API METHOD.

                                Before returning code:

                                generated_controller_method_count == api_count

                                If any API is missing:
                                REGENERATE before returning code.


                                                                                                                                                                                                                                                                    SERVICE CONTRACT IS THE PRIMARY SOURCE OF TRUTH.

                                                                                                                                Extract:

                                                                                                                                METHOD
                                                                                                                                SERVICE_RETURN_TYPE
                                                                                                                                PARAMETERS

                                                                                                                                from the contract.

                                                                                                                                Do not derive them from API definitions.

                                                                                                                                Do not derive them from service code.

                                                                                                                                Use the contract directly.

                                                                                                                                API COVERAGE VALIDATION

                                                                Count all APIs provided.

                                                                For each API:

                                                                1. Verify a controller method exists.
                                                                2. Verify method name exactly equals API NAME.
                                                                3. Verify endpoint path equals API PATH.
                                                                4. Verify HTTP annotation equals API METHOD.

                              Before returning code verify:

generated_method_count == api_count

AND

every_api_name_exists_in_controller

Example:

API:
getPatientsByDoctor

Controller must contain:

getPatientsByDoctor(

If not present:
REGENERATE before returning code.

                                                                If any API is missing:
                                                                REGENERATE before returning code.

                                                                                                                                                                                                                                                                    Metadata:
                                                                                                                                                                                                                                                                    %s
                                                                                                                                                                                                                                                                    """
                                .formatted(
                                                serviceCode,
                                                serviceContract,
                                                metadata.getEntity(),
                                                entityCode,
                                                apiDetails,
                                                metadata);
                System.out.println("===== CONTROLLER PROMPT =====");
                System.out.println(prompt);
                System.out.println("API COUNT = " + apis.size());

                return chatClient.prompt()
                                .user(prompt)
                                .call()
                                .content();
        }
}