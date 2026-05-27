package com.example.springaidemo.dto;

import lombok.Data;

@Data
public class GenerateRequest {

    private String requirement;

    private String entity;

    private String projectPath;
}