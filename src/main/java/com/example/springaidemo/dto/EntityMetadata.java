package com.example.springaidemo.dto;

import lombok.Data;

import java.util.List;

@Data
public class EntityMetadata {

    private String entity;

    private List<FieldMetadata> fields;
}