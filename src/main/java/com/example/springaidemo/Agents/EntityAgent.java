package com.example.springaidemo.Agents;

import com.example.springaidemo.dto.ApiMetadata;
import com.example.springaidemo.dto.EntityMetadata;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityAgent {

    private final ChatClient.Builder builder;

    public String generate(
            EntityMetadata metadata,
            List<ApiMetadata> apis) {

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

        String prompt = """
                                                                                You are an expert Spring Boot JPA Entity generator.


                                                                TABLE NAMING RULES:

                                                Use plural snake_case table names.

                                                Examples:

                                                Order -> orders
                                                User -> users
                                                Employee -> employees
                                                Product -> products

                                                Always generate:


                                                @Table(name = "<plural_name>")

                                                API-DRIVEN ENTITY ENRICHMENT

                                The APIs may require entity fields.

                                Before generating the entity:

                                1. Analyze all APIs.
                                2. Infer required entity attributes.
                                3. Add missing attributes to the entity.

                            FIELD DISCOVERY RULE

                Do not rely on example field names.

                Extract required attributes from:

                - API names
                - API paths
                - API business logic

                Examples are illustrative only.

                The entity must contain every field required
                to implement every API.

                                Do not hardcode these examples.
                                Infer dynamically from API names and API logic.

                                The generated entity must support every generated API.


                                                                            STRICT RULES:

                                                                1. Generate ONLY Java code.
                                                                2. No markdown.
                                                                3. No explanation.
                                                                4. Use Spring Boot 3.
                                                                5. Use ONLY jakarta.persistence imports.
                                                                6. NEVER use javax.persistence.
                                                                7. Use package:
                                                                com.example.generated.entity
                                                                8. Use Lombok.
                                                                7. import all(*) from jakarta.persistence need tables import all as jakarta.persistence.*;


                                                                                com.example.generated.entity
                                                                            API DEFINITIONS:

                                %s

                               FIELD TYPE INFERENCE RULE

                When inferring fields from API names:

                Generate primitive/simple business fields by default.

                Examples:

                getPatientsByDoctor
                -> String doctor

                getUsersByEmail
                -> String email

                getProductsByCategory
                -> String category

                getOrdersByStatus
                -> String status

                Do NOT generate new entity classes.

                Do NOT generate relationships such as:

                @ManyToOne
                @OneToMany
                @OneToOne

                unless explicitly required by the requirement.

                If a field type cannot be determined,
                default to String.
                ENTITY GENERATION SAFETY

                Never create additional entities.

                Generate exactly one entity class.

                Do not reference types that are not:
                - Java built-in types
                - Jakarta types
                - Types explicitly present in the requirements

                If uncertain about a field type,
                use String.
                                ENTITY COMPATIBILITY RULE

                The generated entity must contain all fields required
                by the APIs.

                If an API requires querying, filtering, searching,
                grouping, or updating by a field,
                that field must exist in the entity.

                FIELD DISCOVERY RULE

                Do not rely on example field names.

                Extract required attributes from:

                - API names
                - API paths
                - API business logic

                Examples are illustrative only.

                The entity must contain every field required
                to implement every API.

                These are examples only.

                Do not hardcode them.

                Infer fields dynamically from API names,
                API paths, and API business logic.

                FIELD DISCOVERY RULE

                Analyze API names, paths and business logic.

                Infer all required entity attributes dynamically.

                Do not rely on example field names.

                Before returning the entity:

                Verify that every API can be implemented
                using the generated entity fields.



                                Generate entity class for:

                                %s




                                                                                """
        .formatted(
                        apiDetails,
                        metadata.getEntity());
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}