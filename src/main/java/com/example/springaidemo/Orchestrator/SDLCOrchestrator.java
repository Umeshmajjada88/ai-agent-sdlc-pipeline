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

                    // STEP 2 - Generate Entity
                    String entityCode = entityAgent.generateEntity(requirement);

                    // STEP 3 - Generate Repository
                    String repositoryCode = repositoryAgent.generateRepository(
                                    requirement,
                                    entityCode);

                    // STEP 4 - Generate Service
                    String serviceCode = serviceAgent.generateService(
                                    requirement,
                                    entityCode,
                                    repositoryCode);

                    // STEP 5 - Generate Controller
                    String controllerCode = controllerAgent.generateController(
                                    requirement,
                                    entityCode,
                                    serviceCode);

                    String basePath = "src/main/java/com/example/springaidemo/";

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
                                    Path.of(basePath + "entity/Student.java"),
                                    entityCode);

                    // SAVE REPOSITORY
                    Files.writeString(
                                    Path.of(basePath + "repository/StudentRepository.java"),
                                    repositoryCode);

                    // SAVE SERVICE
                    Files.writeString(
                                    Path.of(basePath + "service/StudentService.java"),
                                    serviceCode);

                    // SAVE CONTROLLER
                    Files.writeString(
                                    Path.of(basePath + "controller/StudentController.java"),
                                    controllerCode);

                    return "Files Generated Successfully";

            } catch (Exception e) {

                    e.printStackTrace();

                    return "Error while generating files";
            }
    }
}