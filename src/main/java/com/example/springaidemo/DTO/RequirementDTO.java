package com.example.springaidemo.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RequirementDTO {

    private String projectType;

    private String entityName;

    private List<FieldDTO> fields;

    private List<String> crudOperations;

    private List<BusinessRuleDTO> businessRules;

    private List<ValidationDTO> validations;

    private List<ServiceMethodDTO> serviceMethods;

    private List<String> repositoryMethods;

    private List<String> workflowStates;

    private List<String> exceptionScenarios;
}

