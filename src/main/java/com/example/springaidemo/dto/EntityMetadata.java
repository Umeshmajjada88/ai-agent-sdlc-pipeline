package com.example.springaidemo.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityMetadata {

    private String entity;

    private List<FieldMetadata> fields;

    private List<ApiMetadata> apis;
}