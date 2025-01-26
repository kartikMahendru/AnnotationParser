package org.homeassignment;

import org.homeassignment.algorithms.impl.JarAnnotationParserASM;
import org.homeassignment.util.JarFilePathUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class JarAnnotationParserASMTest {

    private JarAnnotationParserASM parser;

    @BeforeEach
    void setUp() {
        parser = new JarAnnotationParserASM();
    }

    @Test
    void testScanJarForAnnotations_withValidJarWithAnnotations() {
        // Arrange: Create or mock a JAR file with classes and annotations
        String testJarPath = "src/test/resources/validJarWithAnnotations.jar";
        assertTrue(new File(testJarPath).exists(), "Test JAR file should exist");

        ByteArrayOutputStream outCapture = TestUtils.captureSystemOut();
        parser.scanJarForAnnotations(testJarPath);

        TestUtils.resetSystemOut();
        String output = outCapture.toString();

        // Assert: Validate System.out has expected annotation analysis content
        assertTrue(output.contains("Class:"), "Output should contain Class-level annotations");
        assertTrue(output.contains("Field level annotations"), "Output should include field annotations");
        assertTrue(output.contains("Method level annotations"), "Output should include method annotations");
        assertTrue(output.contains("Parameter level annotations"), "Output should include parameter annotations");
    }

    @Test
    void testScanJarForAnnotations_withValidJarWithoutAnnotations() {
        // Arrange: Create or mock a JAR file without annotations
        String testJarPath = "src/test/resources/validJarWithoutAnnotations.jar";

        ByteArrayOutputStream outCapture = TestUtils.captureSystemOut();
        parser.scanJarForAnnotations(testJarPath);

        TestUtils.resetSystemOut();
        String output = outCapture.toString();

        assertTrue(output.contains("JAR Annotation Analysis:"), "Output should report analysis header");
        assertFalse(output.contains("Class:"), "Output should NOT contain Class-level annotations");
        assertFalse(output.contains("Field level annotations"), "Output should NOT contain field annotations");
        assertFalse(output.contains("Method level annotations"), "Output should NOT contain method annotations");
        assertFalse(output.contains("Parameter level annotations"), "Output should NOT contain parameter annotations");

    }

    @Test
    void testScanJarForAnnotations_withInvalidJarPath() {
        // Arrange
        String invalidJarPath = "invalid/path/to/jarfile.jar";

        ByteArrayOutputStream outCapture = TestUtils.captureSystemOut();

        parser.scanJarForAnnotations(invalidJarPath);
        TestUtils.resetSystemOut();
        String output = outCapture.toString();

        assertFalse(JarFilePathUtil.isValidJarFile(invalidJarPath));
        assertTrue(output.contains("Jar File does not exist"), "Output should display an error message");
        assertTrue(output.contains("invalid/path/to/jarfile.jar"),
                "Output should include the invalid JAR path in the error message");
    }

    @Test
    void testScanJarForAnnotations_withNullJarPath() {
        assertFalse(JarFilePathUtil.isValidJarFile(null));
    }
}
