package com.example.springaidemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceGenerationResult {

    private String serviceCode;

    private String serviceContract;
}