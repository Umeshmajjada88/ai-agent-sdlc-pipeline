package com.example.springaidemo.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileWriterService {

        public void createProjectStructure(String projectPath)
                        throws IOException {

                Path root = Paths.get(projectPath);

                Files.createDirectories(root);

                Files.createDirectories(
                                root.resolve("src/main/java/com/example/generated/entity"));

                Files.createDirectories(
                                root.resolve("src/main/java/com/example/generated/repository"));

                Files.createDirectories(
                                root.resolve("src/main/java/com/example/generated/service"));

                Files.createDirectories(
                                root.resolve("src/main/java/com/example/generated/controller"));

                Files.createDirectories(
                                root.resolve("src/main/java/com/example/generated/exception"));

                Files.createDirectories(
                                root.resolve("src/main/resources"));
        }

        public void writeEntity(
                        String projectPath,
                        String entityName,
                        String content) throws IOException {

                writeFile(
                                projectPath +
                                                "/src/main/java/com/example/generated/entity/"
                                                + entityName
                                                + ".java",
                                content);
        }

        public void writeMainApplication(
                        String projectPath,
                        String content) throws IOException {

                writeFile(
                                projectPath +
                                                "/src/main/java/com/example/generated/GeneratedAppApplication.java",
                                content);
        }

        public void writeRepository(
                        String projectPath,
                        String entityName,
                        String content) throws IOException {

                writeFile(
                                projectPath +
                                                "/src/main/java/com/example/generated/repository/"
                                                + entityName
                                                + "Repository.java",
                                content);
        }

        public void writeService(
                        String projectPath,
                        String entityName,
                        String content) throws IOException {

                writeFile(
                                projectPath +
                                                "/src/main/java/com/example/generated/service/"
                                                + entityName
                                                + "Service.java",
                                content);
        }

        public void writeController(
                        String projectPath,
                        String entityName,
                        String content) throws IOException {

                writeFile(
                                projectPath +
                                                "/src/main/java/com/example/generated/controller/"
                                                + entityName
                                                + "Controller.java",
                                content);
        }

        public void writePom(
                        String projectPath,
                        String content) throws IOException {

                writeFile(
                                projectPath + "/pom.xml",
                                content);
        }

        public void writeProperties(
                        String projectPath,
                        String content) throws IOException {

                writeFile(
                                projectPath +
                                                "/src/main/resources/application.properties",
                                content);
        }

        public void writeException(
                        String projectPath,
                        String fileName,
                        String content) throws IOException {

                writeFile(
                                projectPath +
                                                "/src/main/java/com/example/generated/exception/"
                                                + fileName,
                                content);
        }

        private void writeFile(
                        String path,
                        String content) throws IOException {

                Files.writeString(
                                Paths.get(path),
                                cleanCode(content));
        }

        private String cleanCode(String content) {

                return content
                                .replace("```java", "")
                                .replace("```xml", "")
                                .replace("```properties", "")
                                .replace("```", "")
                                .trim();
        }
}