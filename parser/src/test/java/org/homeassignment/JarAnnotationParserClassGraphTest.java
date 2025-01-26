package org.homeassignment;

import org.homeassignment.algorithms.impl.JarAnnotationParserClassGraph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JarAnnotationParserClassGraphTest {

    private JarAnnotationParserClassGraph parser;

    @BeforeEach
    void setUp() {
        parser = new JarAnnotationParserClassGraph();
    }

    @AfterEach
    void tearDown() {
        parser = null;
    }

    @Test
    void testScanJarForAnnotations_WithValidJar() {
        // Get the JAR file from resources
        String jarPath = Objects.requireNonNull(getClass().getClassLoader().getResource("test-annotations.jar"))
                .getPath();

        assertNotNull(jarPath, "The test JAR file could not be found.");

        // Ensure the method processes a valid JAR without throwing any exceptions
        assertDoesNotThrow(() -> parser.scanJarForAnnotations(jarPath));
    }

    @Test
    void testScanJarForAnnotations_WithEmptyJar() throws Exception {
        // Create an empty JAR file dynamically
        File emptyJar = Files.createTempFile("empty-jar", ".jar").toFile();
        try {
            assertDoesNotThrow(() -> parser.scanJarForAnnotations(emptyJar.getAbsolutePath()));
        } finally {
            // Cleanup
            emptyJar.delete();
        }
    }

    @Test
    void testScanJarForAnnotations_WithInvalidJarPath() {
        // Invalid JAR path
        String invalidJarPath = "invalid-path/nonexistent.jar";

        // Ensure no exception is thrown for invalid paths (method handles errors gracefully)
        assertDoesNotThrow(() -> parser.scanJarForAnnotations(invalidJarPath));
    }

    @Test
    void testScanJarForAnnotations_WithNullJarPath() {
        // Ensure no exception is thrown for null path
        assertDoesNotThrow(() -> parser.scanJarForAnnotations(null));
    }
}