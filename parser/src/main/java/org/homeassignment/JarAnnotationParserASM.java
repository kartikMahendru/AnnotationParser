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

public class JarAnnotationParserASM {

    private static JarAnnotationParserASM instance = null;

    private JarAnnotationParserASM(){}

    public static synchronized JarAnnotationParserASM getInstance()
    {
        if (instance == null)
            instance = new JarAnnotationParserASM();

        return instance;
    }

    public  void scanJarForAnnotations(String jarFilePath) {

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
}



