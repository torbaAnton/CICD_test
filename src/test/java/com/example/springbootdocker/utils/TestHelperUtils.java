package com.example.springbootdocker.utils;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@UtilityClass
public class TestHelperUtils {

    private final String REPLACE_REGEX = "[\\s+\\r\\n]";

    public String getFileContent(String filePath, Class<?> classType) {
        ClassPathResource resource =
                new ClassPathResource(filePath, classType.getClassLoader());
        try {
            return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8).replaceAll(REPLACE_REGEX, "");
        } catch (IOException e) {
            throw new RuntimeException("Cannot read the expected data from json file!", e);
        }
    }
}
