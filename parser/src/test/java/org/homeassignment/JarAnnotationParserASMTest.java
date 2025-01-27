package org.homeassignment;

import org.homeassignment.algorithms.impl.AnnotationLevel;
import org.homeassignment.algorithms.impl.JarAnnotationParserASM;
import org.homeassignment.util.JarFilePathUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JarAnnotationParserASMTest {

    private JarAnnotationParserASM parser;

    @BeforeEach
    void setUp() {
        parser = new JarAnnotationParserASM();
    }

    @Test
    void testScanJarForAnnotations_withValidJarWithAnnotations() {
        // Create or mock a JAR file with classes and annotations
        String testJarPath = "src/test/resources/validJarWithAnnotations.jar";
        assertTrue(new File(testJarPath).exists(), "Test JAR file should exist");

        Map<String, Map<AnnotationLevel, Map<String, Integer>>> responseMap = parser.scanJarForAnnotations(testJarPath);

        // assert that response map should not be empty and should contains all types of annotations
        assertFalse(responseMap.isEmpty(), "Reponse map should contain some annotations data");
        assertFalse(() -> {
            boolean[] result = {false};
            responseMap.forEach((className, internalMap) -> {
                result[0] |= internalMap.get(AnnotationLevel.CLASS) != null && internalMap.get(AnnotationLevel.CLASS).isEmpty();
                result[0] |= internalMap.get(AnnotationLevel.FIELD) != null && internalMap.get(AnnotationLevel.FIELD).isEmpty();
                result[0] |= internalMap.get(AnnotationLevel.METHOD) != null && internalMap.get(AnnotationLevel.METHOD).isEmpty();
                result[0] |= internalMap.get(AnnotationLevel.PARAMETER) != null && internalMap.get(AnnotationLevel.PARAMETER).isEmpty();
            });
            return result[0];
        } , "Response Map should contain all type of annotations");
    }

    @Test
    void testScanJarForAnnotations_withValidJarWithoutAnnotations() {
        String testJarPath = "src/test/resources/validJarWithoutAnnotations.jar";

        Map<String, Map<AnnotationLevel, Map<String, Integer>>> responseMap = parser.scanJarForAnnotations(testJarPath);
        assertTrue(responseMap.isEmpty(), "Reponse map should be empty");

    }

    @Test
    void testScanJarForAnnotations_withInvalidJarPath() {
        String invalidJarPath = "invalid/path/to/jarfile.jar";

        ByteArrayOutputStream outCapture = TestUtils.captureSystemOut();

        Map<String, Map<AnnotationLevel, Map<String, Integer>>> responseMap = parser.scanJarForAnnotations(invalidJarPath);
        TestUtils.resetSystemOut();
        String output = outCapture.toString();

        assertFalse(JarFilePathUtil.isValidJarFile(invalidJarPath));
        assertNull(responseMap);
    }

    @Test
    void testScanJarForAnnotations_withNullJarPath() {
        assertFalse(JarFilePathUtil.isValidJarFile(null));
        assertNull(parser.scanJarForAnnotations(null));
    }
}
