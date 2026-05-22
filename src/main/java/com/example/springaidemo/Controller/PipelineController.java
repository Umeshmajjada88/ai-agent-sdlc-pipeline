package com.example.springaidemo.Controller;



import com.example.springaidemo.Orchestrator.SDLCOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PipelineController {

    private final SDLCOrchestrator orchestrator;

    @PostMapping("/generate")
    public String generate(@RequestBody String input) {

        return orchestrator.execute(input);
    }
}
