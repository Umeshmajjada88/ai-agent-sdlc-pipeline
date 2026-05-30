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
    public static String addPackage(String response, String entityName){
        return "package com.example.springaidemo.controller;\n import com.example.springaidemo.entity.%s;\n" + //
                        "                import com.example.springaidemo.service.%sService;\n" + //
                        "\n" + //
                        "                import org.springframework.http.ResponseEntity;\n" + //
                        "                import org.springframework.web.bind.annotation.*;\n" + //
                        "\n" + //
                        "                import java.util.List;\n" + //
                        "                import java.util.Optional;".formatted(entityName) + response;
                        
    }
}