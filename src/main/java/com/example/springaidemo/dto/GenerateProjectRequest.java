package com.example.springaidemo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateProjectRequest {

    @NotBlank
    private String requirement;

    @NotBlank
    private String projectPath;
}