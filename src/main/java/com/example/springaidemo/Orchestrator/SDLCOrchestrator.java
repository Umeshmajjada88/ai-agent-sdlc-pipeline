package com.example.springaidemo.Orchestrator;


import com.example.springaidemo.Agents.BackendAgent;
import com.example.springaidemo.Agents.RequirementAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SDLCOrchestrator {

    private final RequirementAgent requirementAgent;
    private final BackendAgent backendAgent;

    public String execute(String input) {

        // STEP 1
        String analyzedRequirement =
                requirementAgent.analyze(input);

        System.out.println("Requirement Analysis:");
        System.out.println(analyzedRequirement);

        // STEP 2
        String generatedCode =
                backendAgent.generateCrudCode(analyzedRequirement);

        System.out.println("Generated Code:");
        System.out.println(generatedCode);

        return generatedCode;
    }
}