package com.example.springaidemo.Orchestrator;

import com.example.springaidemo.Agents.RequirementAgent;
import com.example.springaidemo.dto.EntityMetadata;
import com.example.springaidemo.dto.GenerateProjectResponse;
import com.example.springaidemo.service.ProjectGeneratorService;
import com.example.springaidemo.service.ProjectRunnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SDLCOrchestrator {

        private final RequirementAgent requirementAgent;
        private final ProjectGeneratorService generatorService;
        private final ProjectRunnerService runnerService;

        public GenerateProjectResponse generate(
                        String requirement,
                        String path) throws Exception {

                String aiResponse = requirementAgent.analyze(requirement);

                System.out.println(aiResponse);

                ObjectMapper mapper = new ObjectMapper();

                aiResponse = aiResponse
                                .replace("```json", "")
                                .replace("```", "")
                                .trim();

                System.out.println(aiResponse);

                EntityMetadata metadata = mapper.readValue(aiResponse, EntityMetadata.class);

                generatorService.generateProject(path, metadata);

                // runnerService.runProject(path);

                return GenerateProjectResponse.builder()
                                .status("SUCCESS")
                                .projectPath(path)
                                .runningUrl("http://localhost:8000")
                                .build();
        }
}