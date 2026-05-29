package com.example.springaidemo.dto;

import java.util.List;

import lombok.Data;


@Data
public class GenerateRequest {

    private String requirement;

    private String entity;

    private String projectPath;

    private List<ApiMetadata> apis;
}