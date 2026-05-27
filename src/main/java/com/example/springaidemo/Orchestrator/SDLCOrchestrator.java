package com.example.springaidemo.Orchestrator;

import com.example.springaidemo.Agents.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class SDLCOrchestrator {

        private final RequirementAgent requirementAgent;
        private final EntityAgent entityAgent;
        private final RepositoryAgent repositoryAgent;
        private final ServiceAgent serviceAgent;
        private final ControllerAgent controllerAgent;

        public String execute(String input) {

                try {

                        // STEP 1 - Analyze Requirement
                        String requirement = requirementAgent.analyze(input);
                        String entityName = extractEntityName(requirement);

                        // STEP 2 - Generate Entity
                        String entityCode = entityAgent.generateEntity(requirement);

                        // STEP 3 - Generate Repository
                        String repositoryCode = repositoryAgent.generateRepository(
                                        requirement,
                                        entityCode,
                                        entityName);

                        // STEP 4 - Generate Service
                        String serviceCode = serviceAgent.generateService(
                                        requirement,
                                        entityCode,
                                        repositoryCode,
                                        entityName);

                        // STEP 5 - Generate Controller
                        String controllerCode = controllerAgent.generateController(
                                        requirement,
                                        entityCode,
                                        serviceCode,
                                        entityName);

                        // GENERATED PROJECT PATH
                        String basePath = "generated-project/src/main/java/com/example/generated/";

                        // CREATE DIRECTORIES
                        Files.createDirectories(
                                        Path.of(basePath + "entity"));

                        Files.createDirectories(
                                        Path.of(basePath + "repository"));

                        Files.createDirectories(
                                        Path.of(basePath + "service"));

                        Files.createDirectories(
                                        Path.of(basePath + "controller"));

                        // SAVE ENTITY
                        Files.writeString(
                                        Path.of(basePath + "entity/" + entityName
                                                        + ".java"),
                                        entityCode);

                        // SAVE REPOSITORY
                        Files.writeString(
                                        Path.of(basePath + "repository/"+ entityName
                                                        + "Repository.java"),
                                        repositoryCode);

                        // SAVE SERVICE
                        Files.writeString(
                                        Path.of(basePath + "service/" + entityName
                                                        + "Service.java"),
                                        serviceCode);

                        // SAVE CONTROLLER
                        Files.writeString(
                                        Path.of(basePath + "controller/" + entityName
                                                        + "Controller.java"),
                                        controllerCode);

                        return "Files Generated Successfully";

                } catch (Exception e) {

                        e.printStackTrace();

                        return "Error while generating files";
                }
        }
        
        private String extractEntityName(String requirement) {

                String[] lines = requirement.split("\\n");

                for (String line : lines) {

                        if (line.startsWith("Entity Name:")) {

                                return line.replace(
                                                "Entity Name:",
                                                "").trim();
                        }
                }

                throw new RuntimeException(
                                "Entity Name not found in requirement");
        }
}