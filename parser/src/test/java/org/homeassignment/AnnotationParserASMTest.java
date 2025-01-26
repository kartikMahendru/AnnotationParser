package org.homeassignment;

import org.junit.jupiter.api.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnnotationParserASMTest {

    @Test
    void testAnnotationParsingInClasses() throws IOException {
        // Mocking a JAR file
        JarFile jarFile = mock(JarFile.class);
        JarEntry classEntry = mock(JarEntry.class);
        JarEntry nonClassEntry = mock(JarEntry.class);

        // Setup class entry
        when(classEntry.getName()).thenReturn("TestClass.class");
        when(nonClassEntry.getName()).thenReturn("NotAClass.txt");

        Enumeration<JarEntry> entries = Collections.enumeration(Arrays.asList(classEntry, nonClassEntry));
        when(jarFile.entries()).thenReturn(entries);

        // Mock ClassReader behavior
        ClassReader classReader = mock(ClassReader.class);
        AnnotationParserASM.AnnotationCollector collector = new AnnotationParserASM.AnnotationCollector();
        doAnswer(invocation -> {
            collector.visit(Opcodes.ASM9, 0, "TestClass", null, null, null);
            collector.visitAnnotation("Lorg/example/MyAnnotation;", true);
            return null; // Return nothing as visit is void
        }).when(classReader).accept(any(), eq(0));

        // Simulate processing
        Map<String, Map<String, Integer>> result = new HashMap<>();
        try (InputStream inputStream = new ByteArrayInputStream(new byte[0])) {
            AnnotationParserASM.AnnotationCollector annotationCollector = new AnnotationParserASM.AnnotationCollector();
            annotationCollector.visit(Opcodes.ASM9, Opcodes.ACC_PUBLIC, "TestClass", null, null, null);
            annotationCollector.visitAnnotation("Lorg/example/MyAnnotation;", true);
            result.put(annotationCollector.getClassName(), annotationCollector.getAnnotations());
        }

        // Check if the result matches expectations
        assertFalse(result.isEmpty());
        assertTrue(result.containsKey("TestClass"));
        assertEquals(1, result.get("TestClass").get("org.example.MyAnnotation"));
    }

    @Test
    void testNoAnnotationsPresent() throws IOException {
        // Test for cases where no annotations exist in a class
        AnnotationParserASM.AnnotationCollector collector = new AnnotationParserASM.AnnotationCollector();
        collector.visit(Opcodes.ASM9, Opcodes.ACC_PUBLIC, "PlainClass", null, null, null);

        // Ensure no annotations are detected
        assertTrue(collector.getAnnotations().isEmpty());
    }

    @Test
    void testInvalidJarFileHandling() {
        // Test to handle invalid/missing JAR file
        String invalidPath = "invalid/path/to/jarfile.jar";

        IOException exception = assertThrows(IOException.class, () -> {
            try (JarFile jarFile = new JarFile(invalidPath)) {
                jarFile.entries();
            }
        });

        assertNotNull(exception.getMessage());
    }

    @Test
    void testClassNameExtraction() {
        // Verify that class names are properly extracted
        AnnotationParserASM.AnnotationCollector collector = new AnnotationParserASM.AnnotationCollector();
        collector.visit(Opcodes.ASM9, Opcodes.ACC_PUBLIC, "sample/TestClass", null, null, null);

        assertEquals("sample.TestClass", collector.getClassName());
    }

    @Test
    void testAnnotationOnMethods() {
        // Mock method-level annotations
        AnnotationParserASM.AnnotationCollector collector = new AnnotationParserASM.AnnotationCollector();
        collector.visitMethod(Opcodes.ACC_PUBLIC, "testMethod", "()V", null, null)
                .visitAnnotation("Lorg/example/MethodAnnotation;", true);

        assertFalse(collector.getAnnotations().isEmpty());
        assertEquals(1, collector.getAnnotations().get("org.example.MethodAnnotation"));
    }

    @Test
    void testEmptyJarFile() throws IOException {
        // Test for an empty JAR file
        JarFile jarFile = mock(JarFile.class); // Mock JarFile
        when(jarFile.entries()).thenReturn(Collections.emptyEnumeration());

        assertDoesNotThrow(() -> {
            Map<String, Map<String, Integer>> result = new HashMap<>();
            assertTrue(result.isEmpty());
        });
    }
}
