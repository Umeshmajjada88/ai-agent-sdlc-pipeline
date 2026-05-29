package com.example.springaidemo.dto;

import lombok.Data;

@Data
public class ApiMetadata {

    private String name;

    private String method;

    private String path;

    private String logic;
}