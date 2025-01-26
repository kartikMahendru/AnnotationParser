package org.homeassignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import java.lang.annotation.Annotation;

public class JarAnnotationParserV1 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java JarAnnotationScanner <path-to-jar>");
            return;
        }

        String jarFilePath = args[0];
        File jarFile = new File(jarFilePath);

        if (!jarFile.exists() || !jarFilePath.endsWith(".jar")) {
            System.out.println("Invalid JAR file path provided.");
            return;
        }

        try {
            Map<String, List<String>> classAnnotationsMap = scanJarFile(jarFilePath);

            // Print the result
            classAnnotationsMap.forEach((className, annotations) -> {
                System.out.println("Class: " + className);
                System.out.println("Annotations: " + annotations);
                System.out.println("-------------");
            });

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<String>> scanJarFile(String jarFilePath) throws IOException, ClassNotFoundException {
        Map<String, List<String>> classAnnotationsMap = new HashMap<>();

        try (JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFilePath))) {
            JarEntry entry;

            while ((entry = jarInputStream.getNextJarEntry()) != null) {
                String name = entry.getName();

                // Check if the entry is a class file
                if (name.endsWith(".class")) {
                    String className = name.replace("/", ".").replace(".class", "");

                    // Load the class
                    Class<?> clazz = Class.forName(className, false, JarAnnotationParserV1.class.getClassLoader());

                    // Get annotations
                    Annotation[] annotations = clazz.getAnnotations();
                    List<String> annotationNames = new ArrayList<>();

                    for (Annotation annotation : annotations) {
                        annotationNames.add(annotation.annotationType().getName());
                    }

                    // Add to the map
                    classAnnotationsMap.put(className, annotationNames);
                }
            }
        }

        return classAnnotationsMap;
    }
}