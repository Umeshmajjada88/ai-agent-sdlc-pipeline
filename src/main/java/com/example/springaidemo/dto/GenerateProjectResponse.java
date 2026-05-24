package com.example.springaidemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateProjectResponse {

    private String status;
    private String projectPath;
    private String runningUrl;
}