package com.example.springaidemo.Agents;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigAgent {

    private final ChatClient.Builder builder;

    public String generatePom() {

        return """
                <?xml version="1.0" encoding="UTF-8"?>

                <project xmlns="http://maven.apache.org/POM/4.0.0"
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                        https://maven.apache.org/xsd/maven-4.0.0.xsd">

                    <modelVersion>4.0.0</modelVersion>

                    <parent>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-parent</artifactId>
                        <version>3.4.5</version>
                        <relativePath/>
                    </parent>

                    <groupId>com.example</groupId>
                    <artifactId>generated-app</artifactId>
                    <version>1.0.0</version>

                    <properties>
                        <java.version>17</java.version>
                    </properties>

                    <dependencies>

                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-web</artifactId>
                        </dependency>

                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-data-jpa</artifactId>
                        </dependency>

                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-validation</artifactId>
                        </dependency>

                        <dependency>
                            <groupId>com.h2database</groupId>
                            <artifactId>h2</artifactId>
                            <scope>runtime</scope>
                        </dependency>

                        <dependency>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>1.18.38</version>
                            <optional>true</optional>
                        </dependency>

                    </dependencies>

                    <build>
                        <plugins>

                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                            </plugin>

                            <plugin>
                                <groupId>org.apache.maven.plugins</groupId>
                                <artifactId>maven-compiler-plugin</artifactId>
                                <version>3.13.0</version>

                                <configuration>
                                    <source>17</source>
                                    <target>17</target>
                                </configuration>

                            </plugin>

                        </plugins>
                    </build>

                </project>
                """;
    }

    public String generateProperties() {

        ChatClient chatClient = builder.build();

        String prompt = """
                Generate ONLY application.properties.

                Configure:
                - server.port=8000
                - h2 database
                - hibernate
                - show sql

                No markdown.
                No explanation.
                """;

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}