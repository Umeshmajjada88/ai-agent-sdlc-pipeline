package com.example.springaidemo.Orchestrator;

import com.example.springaidemo.Agents.*;
import com.example.springaidemo.DTO.RequirementDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SDLCOrchestrator {

        private final RequirementAgent requirementAgent;
        private final EntityAgent entityAgent;
        private final RepositoryAgent repositoryAgent;
        private final ServiceAgent serviceAgent;
        private final ControllerAgent controllerAgent;
        private final FeignClientAgent feignClientAgent;


        public String execute(String input) {

                try {

                        // STEP 1 - Analyze Requirement
                        RequirementDTO requirement = requirementAgent.analyze(input);
                        String entityName = requirement.getEntityName();
                        
                        String feignCode = feignClientAgent.generateFeignClient(
                                        requirement);
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
                        String basePath = "/Users/devangkulkarni/genai/springaidemo/src/main/java/com/example/springaidemo/";

                        // CREATE DIRECTORIES
                        Files.createDirectories(
                                        Path.of(basePath + "entity"));

                        Files.createDirectories(
                                        Path.of(basePath + "repository"));

                        Files.createDirectories(
                                        Path.of(basePath + "service"));

                        Files.createDirectories(
                                        Path.of(basePath + "controller"));

                        Files.createDirectories(
                                        Path.of(basePath + "feign"));

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

                        // SAVE FEIGN CLIENT
                        Files.writeString(
                                        Path.of(basePath + "feign/PaymentClient.java"),
                                        feignCode);

                        return "Files Generated Successfully";

                } catch (Exception e) {

                        e.printStackTrace();

                        return "Error while generating files";
                }
        }
        
        private String extractEntityName(String requirement) {

                Pattern pattern = Pattern.compile(
                                "Entity\\s*Name\\s*:\\s*(\\w+)",
                                Pattern.CASE_INSENSITIVE);

                Matcher matcher = pattern.matcher(requirement);

                if (matcher.find()) {

                        return matcher.group(1).trim();
                }

                // FALLBACK
                return "Student";
        }
}