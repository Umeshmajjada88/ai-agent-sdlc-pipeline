package com.example.springaidemo.Orchestrator;

import com.example.springaidemo.Agents.ConfigAgent;
import com.example.springaidemo.Agents.ControllerAgent;
import com.example.springaidemo.Agents.EntityAgent;
import com.example.springaidemo.Agents.RepositoryAgent;
import com.example.springaidemo.Agents.RequirementAgent;
import com.example.springaidemo.Agents.ServiceAgent;
import com.example.springaidemo.dto.ApiMetadata;
import com.example.springaidemo.dto.EntityMetadata;
import com.example.springaidemo.dto.GenerateProjectResponse;
import com.example.springaidemo.dto.ServiceGenerationResult;
import com.example.springaidemo.service.FileWriterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SDLCOrchestrator {
        //hoo

    private final RequirementAgent requirementAgent;

    private final EntityAgent entityAgent;
    private final RepositoryAgent repositoryAgent;
    private final ServiceAgent serviceAgent;
    private final ControllerAgent controllerAgent;
    private final ConfigAgent configAgent;

    private final FileWriterService fileWriterService;

 public GenerateProjectResponse generate(

        String requirement,
        String entity,
        String projectPath,
        List<ApiMetadata> apis)throws Exception {

        // STEP 1 — Analyze Requirement
        String metadataJson =
                requirementAgent.analyze(requirement);

        metadataJson = metadataJson
                .replace("```json", "")
                .replace("```", "")
                .trim();

        

        // STEP 2 — Convert JSON to DTO
        ObjectMapper mapper = new ObjectMapper();

        EntityMetadata metadata =
                mapper.readValue(
                        metadataJson,
                        EntityMetadata.class
                );
                metadata.setEntity(entity);

        String entityName = metadata.getEntity();
        String plural = entityName + "s";

String entityCode =
        entityAgent.generate(
        metadata,
        apis
);
ServiceGenerationResult result =
        serviceAgent.generate(
                metadata,
                apis,
                entityCode
        );

String serviceCode =
        result.getServiceCode();

String serviceContract =
        result.getServiceContract();
System.out.println("===== SERVICE CONTRACT =====");
System.out.println(serviceContract);

validateServiceMethods(serviceCode, apis);

String repositoryCode =
        repositoryAgent.generate(
                metadata,
                entityCode,
                serviceCode,
                serviceContract
        );

String controllerCode =
        controllerAgent.generate(
                metadata,
                entityCode,
                apis,
                serviceCode,
                serviceContract
        );

validateControllerMethods(controllerCode, apis);


        String pomCode =
                configAgent.generatePom();

        String propertiesCode =
                configAgent.generateProperties();


        // STEP 4 — Create Folder Structure
        fileWriterService.createProjectStructure(projectPath);

        // STEP 5 — Write Files
        fileWriterService.writeEntity(
                projectPath,
                entityName,
                entityCode
        );

        fileWriterService.writeRepository(
                projectPath,
                entityName,
                repositoryCode
        );

        fileWriterService.writeService(
                projectPath,
                entityName,
                serviceCode
        );

        fileWriterService.writeController(
                projectPath,
                entityName,
                controllerCode
        );

        fileWriterService.writePom(
                projectPath,
                pomCode
        );

        fileWriterService.writeProperties(
                projectPath,
                propertiesCode
        );

        // STEP 6 — Generate Main Class
        String mainClass = """
                package com.example.generated;

                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;

                @SpringBootApplication
                public class GeneratedAppApplication {

                    public static void main(String[] args) {

                        SpringApplication.run(
                                GeneratedAppApplication.class,
                                args
                        );
                    }
                }
                """;

        fileWriterService.writeMainApplication(
                projectPath,
                mainClass
        );

        return GenerateProjectResponse.builder()
                .status("SUCCESS")
                .projectPath(projectPath)
                .runningUrl("http://localhost:8000")
                .build();
    }
private void validateServiceMethods(
        String serviceCode,
        List<ApiMetadata> apis) {

    for (ApiMetadata api : apis) {

        if (!serviceCode.contains(" " + api.getName() + "(")) {

            throw new RuntimeException(
                    "Service generation skipped API: "
                            + api.getName()
            );
        }
    }
}

private void validateControllerMethods(
        String controllerCode,
        List<ApiMetadata> apis) {

    for (ApiMetadata api : apis) {

        if (!controllerCode.contains(" " + api.getName() + "(")) {

            throw new RuntimeException(
                    "Controller generation skipped API: "
                            + api.getName()
            );
        }
    }
}
}
