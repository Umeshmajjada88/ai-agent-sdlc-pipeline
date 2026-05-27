package com.example.springaidemo.Orchestrator;

import com.example.springaidemo.Agents.ConfigAgent;
import com.example.springaidemo.Agents.ControllerAgent;
import com.example.springaidemo.Agents.EntityAgent;
import com.example.springaidemo.Agents.RepositoryAgent;
import com.example.springaidemo.Agents.RequirementAgent;
import com.example.springaidemo.Agents.ServiceAgent;
import com.example.springaidemo.dto.ApiContract;
import com.example.springaidemo.dto.EntityMetadata;
import com.example.springaidemo.dto.GenerateProjectResponse;
import com.example.springaidemo.service.FileWriterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
        String projectPath)throws Exception {

        // STEP 1 — Analyze Requirement
        String metadataJson =
                requirementAgent.analyze(requirement);

        metadataJson = metadataJson
                .replace("```json", "")
                .replace("```", "")
                .trim();

        System.out.println(metadataJson);

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

ApiContract apiContract =
        ApiContract.builder()
                .createMethod("create" + entityName)
                .getAllMethod("getAll" + plural)
                .getByIdMethod("get" + entityName + "ById")
                .updateMethod("update" + entityName)
                .deleteMethod("delete" + entityName)
                .build();

        // STEP 3 — Generate Files
        String entityCode =
                entityAgent.generate(metadata);

        String repositoryCode =
                repositoryAgent.generate(metadata);

       String serviceCode =
        serviceAgent.generate(
                metadata,
                apiContract
        );

String controllerCode =
        controllerAgent.generate(
                metadata,
                apiContract
        );

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
}