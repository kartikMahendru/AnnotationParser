package org.homeassignment;

import org.objectweb.asm.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AnnotationParserASM {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java JarAnnotationScanner <path-to-jar>");
            return;
        }

        String jarFilePath = args[0];
        Map<String, Map<String, Integer>> classAnnotationMap = new HashMap<>();

        try (JarFile jarFile = new JarFile(new File(jarFilePath))) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    try (FileInputStream inputStream = new FileInputStream(new File(jarFilePath))) {
                        ClassReader classReader = new ClassReader(jarFile.getInputStream(entry));
                        AnnotationCollector annotationCollector = new AnnotationCollector();
                        classReader.accept(annotationCollector, 0);

                        if (!annotationCollector.getAnnotations().isEmpty()) {
                            classAnnotationMap.put(
                                    annotationCollector.getClassName(),
                                    annotationCollector.getAnnotations()
                            );
                        }
                    } catch (IOException e) {
                        System.out.println("Error processing class file: " + entry.getName());
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read the JAR file: " + jarFilePath);
            e.printStackTrace();
        }

        // Print the analysis
        System.out.println("JAR Annotation Analysis:");
        classAnnotationMap.forEach((className, annotationMap) -> {
            System.out.println("Class: " + className);
            annotationMap.forEach((annotation, count) ->
                    System.out.println("  " + annotation + ": " + count));
        });
    }

    static class AnnotationCollector extends ClassVisitor {
        private String className;
        private final Map<String, Integer> annotations = new HashMap<>();

        public AnnotationCollector() {
            super(Opcodes.ASM9);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.className = name.replace('/', '.');
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            String annotationName = Type.getType(descriptor).getClassName();
            annotations.put(annotationName, annotations.getOrDefault(annotationName, 0) + 1);
            return super.visitAnnotation(descriptor, visible);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            return new MethodVisitor(Opcodes.ASM9) {
                @Override
                public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                    String annotationName = Type.getType(descriptor).getClassName();
                    annotations.put(annotationName, annotations.getOrDefault(annotationName, 0) + 1);
                    return super.visitAnnotation(descriptor, visible);
                }
            };
        }

        public String getClassName() {
            return className;
        }

        public Map<String, Integer> getAnnotations() {
            return annotations;
        }
    }
}

