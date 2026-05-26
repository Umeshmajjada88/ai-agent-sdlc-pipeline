package com.example.springaidemo.utils;

public class AIResponseCleaner {

    public static String clean(String response) {

        // REMOVE MARKDOWN
        response = response.replace("```java", "");
        response = response.replace("```", "");

        // REMOVE EXTRA SPACES
        response = response.trim();

        return response;
    }
}