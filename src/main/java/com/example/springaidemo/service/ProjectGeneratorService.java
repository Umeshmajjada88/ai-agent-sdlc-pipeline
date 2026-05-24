package com.example.springaidemo.service;

import com.example.springaidemo.dto.EntityMetadata;
import com.example.springaidemo.dto.FieldMetadata;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProjectGeneratorService {

    public void generateProject(
            String projectPath,
            EntityMetadata metadata) throws IOException {

        Path root = Paths.get(projectPath);

        Files.createDirectories(root);

        Files.createDirectories(root.resolve("src/main/java/com/example/generated/controller"));
        Files.createDirectories(root.resolve("src/main/java/com/example/generated/entity"));
        Files.createDirectories(root.resolve("src/main/java/com/example/generated/repository"));
        Files.createDirectories(root.resolve("src/main/java/com/example/generated/service"));
        Files.createDirectories(root.resolve("src/main/resources"));

        createPom(root);
        createProperties(root);
        createMain(root);

        createEntity(root, metadata);
        createRepository(root, metadata);
        createService(root, metadata);
        createController(root, metadata);
    }

    private void createPom(Path root) throws IOException {

        String pom = """
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
                    </parent>

                    <groupId>com.example</groupId>
                    <artifactId>generated-app</artifactId>
                    <version>1.0</version>

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
                            <optional>true</optional>
                        </dependency>

                    </dependencies>

                </project>
                """;

        Files.writeString(root.resolve("pom.xml"), pom);
    }

    private void createProperties(Path root) throws IOException {

        String properties = """
                server.port=8000

                spring.datasource.url=jdbc:h2:mem:testdb
                spring.datasource.driverClassName=org.h2.Driver
                spring.datasource.username=sa
                spring.datasource.password=

                spring.h2.console.enabled=true

                spring.jpa.hibernate.ddl-auto=update
                spring.jpa.show-sql=true
                """;

        Files.writeString(
                root.resolve("src/main/resources/application.properties"),
                properties
        );
    }

    private void createMain(Path root) throws IOException {

        String main = """
                package com.example.generated;

                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;

                @SpringBootApplication
                public class GeneratedApplication {

                    public static void main(String[] args) {
                        SpringApplication.run(GeneratedApplication.class, args);
                    }
                }
                """;

        Files.writeString(
                root.resolve(
                        "src/main/java/com/example/generated/GeneratedApplication.java"
                ),
                main
        );
    }

    private void createEntity(
            Path root,
            EntityMetadata metadata) throws IOException {

        String entityName = metadata.getEntity();

        StringBuilder fields = new StringBuilder();

        for (FieldMetadata field : metadata.getFields()) {

            if (field.getName().equalsIgnoreCase("id")) {

                fields.append("""
                        @Id
                        @GeneratedValue(strategy = GenerationType.IDENTITY)
                        """);
            }

            fields.append(
                    "private "
                            + field.getType()
                            + " "
                            + field.getName()
                            + ";\n\n"
            );
        }

        String entity = """
                package com.example.generated.entity;

                import jakarta.persistence.*;
                import lombok.*;

                @Entity
                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public class %s {

                    %s
                }
                """.formatted(
                entityName,
                fields.toString()
        );

        Files.writeString(
                root.resolve(
                        "src/main/java/com/example/generated/entity/"
                                + entityName
                                + ".java"
                ),
                entity
        );
    }

    private void createRepository(
            Path root,
            EntityMetadata metadata) throws IOException {

        String entityName = metadata.getEntity();

        String repository = """
                package com.example.generated.repository;

                import com.example.generated.entity.%s;
                import org.springframework.data.jpa.repository.JpaRepository;

                public interface %sRepository
                        extends JpaRepository<%s, Long> {
                }
                """.formatted(
                entityName,
                entityName,
                entityName
        );

        Files.writeString(
                root.resolve(
                        "src/main/java/com/example/generated/repository/"
                                + entityName
                                + "Repository.java"
                ),
                repository
        );
    }

    private void createService(
            Path root,
            EntityMetadata metadata) throws IOException {

        String entityName = metadata.getEntity();

        String service = """
                package com.example.generated.service;

                import com.example.generated.entity.%s;
                import com.example.generated.repository.%sRepository;
                import lombok.RequiredArgsConstructor;
                import org.springframework.stereotype.Service;

                import java.util.List;

                @Service
                @RequiredArgsConstructor
                public class %sService {

                    private final %sRepository repository;

                    public List<%s> getAll() {
                        return repository.findAll();
                    }

                    public %s save(%s entity) {
                        return repository.save(entity);
                    }
                }
                """.formatted(
                entityName,
                entityName,
                entityName,
                entityName,
                entityName,
                entityName,
                entityName
        );

        Files.writeString(
                root.resolve(
                        "src/main/java/com/example/generated/service/"
                                + entityName
                                + "Service.java"
                ),
                service
        );
    }

    private void createController(
            Path root,
            EntityMetadata metadata) throws IOException {

        String entityName = metadata.getEntity();

        String endpoint =
                entityName.toLowerCase() + "s";

        String controller = """
                package com.example.generated.controller;

                import com.example.generated.entity.%s;
                import com.example.generated.service.%sService;
                import lombok.RequiredArgsConstructor;
                import org.springframework.web.bind.annotation.*;

                import java.util.List;

                @RestController
                @RequestMapping("/%s")
                @RequiredArgsConstructor
                public class %sController {

                    private final %sService service;

                    @GetMapping
                    public List<%s> getAll() {
                        return service.getAll();
                    }

                    @PostMapping
                    public %s save(@RequestBody %s entity) {
                        return service.save(entity);
                    }
                }
                """.formatted(
                entityName,
                entityName,
                endpoint,
                entityName,
                entityName,
                entityName,
                entityName,
                entityName
        );

        Files.writeString(
                root.resolve(
                        "src/main/java/com/example/generated/controller/"
                                + entityName
                                + "Controller.java"
                ),
                controller
        );
    }
}