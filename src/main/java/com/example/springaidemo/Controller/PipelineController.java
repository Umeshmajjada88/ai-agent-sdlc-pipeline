package com.example.springaidemo.Controller;

import com.example.springaidemo.dto.GenerateProjectRequest;
import com.example.springaidemo.dto.GenerateProjectResponse;
import com.example.springaidemo.dto.GenerateRequest;
import com.example.springaidemo.Orchestrator.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PipelineController {
    // joo

    private final SDLCOrchestrator orchestrator;

    @PostMapping("/generate-project")
public GenerateProjectResponse generate(
        @RequestBody GenerateRequest request)
        throws Exception {

return orchestrator.generate(
        request.getRequirement(),
        request.getEntity(),
        request.getProjectPath(),
        request.getApis()
);
}
}

class ReqesBody { 
    String name = "";
    
}