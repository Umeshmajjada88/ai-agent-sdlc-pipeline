package com.example.springaidemo.DTO;

import lombok.Data;

import java.util.List;

@Data
public class RequirementDTO {

    private String entityName;

    private List<FieldDTO> fields;

    private List<String> crudOperations;
}