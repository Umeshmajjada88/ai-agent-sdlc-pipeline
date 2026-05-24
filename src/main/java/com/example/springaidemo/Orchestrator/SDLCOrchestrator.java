package com.example.springaidemo.Orchestrator;

import com.example.springaidemo.Agents.RequirementAgent;
import com.example.springaidemo.dto.GenerateProjectResponse;
import com.example.springaidemo.service.ProjectGeneratorService;
import com.example.springaidemo.service.ProjectRunnerService;
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

        String metadata =
                requirementAgent.analyze(requirement);

        System.out.println(metadata);

        generatorService.generateProject(path);

        runnerService.runProject(path);

        return GenerateProjectResponse.builder()
                .status("SUCCESS")
                .projectPath(path)
                .runningUrl("http://localhost:8000")
                .build();
    }
}