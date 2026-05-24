package com.example.springaidemo.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProjectRunnerService {

    public void runProject(String projectPath)
            throws IOException {

        ProcessBuilder builder = new ProcessBuilder();

        builder.directory(new java.io.File(projectPath));

        builder.command("mvn", "spring-boot:run");

        builder.inheritIO();

        builder.start();
    }
}