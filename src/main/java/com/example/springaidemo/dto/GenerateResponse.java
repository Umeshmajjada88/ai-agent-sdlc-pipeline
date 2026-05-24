package com.example.springaidemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GenerateResponse {

    private String analyzedRequirement;
    private String generatedCode;
}