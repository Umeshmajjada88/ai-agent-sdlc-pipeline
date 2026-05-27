package com.example.springaidemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiContract {

    private String createMethod;
    private String getAllMethod;
    private String getByIdMethod;
    private String updateMethod;
    private String deleteMethod;
}