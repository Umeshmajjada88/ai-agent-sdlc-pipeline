package com.example.springaidemo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateRequest {

    @NotBlank(message = "Requirement cannot be empty")
    private String requirement;
}