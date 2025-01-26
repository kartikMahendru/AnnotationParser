package org.homeassignment.algorithms.impl;

import org.homeassignment.util.AnnotationCollector;
import org.homeassignment.algorithms.JarAnnotationParser;
import org.homeassignment.util.JarFilePathUtil;
import org.objectweb.asm.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

// parse jar for classes implementing annotations using ASM
public class JarAnnotationParserASM implements JarAnnotationParser {

    private final Map<String, Map<String, Integer>> classAnnotationMap = new HashMap<>();
    private final Map<String, Map<String, Integer>> fieldAnnotationMap = new HashMap<>();
    private final Map<String, Map<String, Integer>> methodAnnotationMap = new HashMap<>();
    private final Map<String, Map<String, Integer>> parameterAnnotationMap = new HashMap<>();

    public  void scanJarForAnnotations(String jarFilePath) {

        if(JarFilePathUtil.isValidJarFile(jarFilePath)) {

            try (JarFile jarFile = new JarFile(new File(jarFilePath))) {
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        try (InputStream inputStream = jarFile.getInputStream(entry)) {
                            ClassReader classReader = new ClassReader(inputStream);
                            AnnotationCollector annotationCollector = new AnnotationCollector();
                            classReader.accept(annotationCollector, 0);

                            if (!annotationCollector.getClassAnnotations().isEmpty()) {
                                classAnnotationMap.put(
                                        annotationCollector.getClassName(),
                                        annotationCollector.getClassAnnotations()
                                );
                            }

                            if (!annotationCollector.getFieldAnnotations().isEmpty()) {
                                fieldAnnotationMap.put(
                                        annotationCollector.getClassName(),
                                        annotationCollector.getFieldAnnotations()
                                );
                            }

                            if (!annotationCollector.getMethodAnnotations().isEmpty()) {
                                methodAnnotationMap.put(
                                        annotationCollector.getClassName(),
                                        annotationCollector.getMethodAnnotations()
                                );
                            }

                            if (!annotationCollector.getParameterAnnotations().isEmpty()) {
                                parameterAnnotationMap.put(
                                        annotationCollector.getClassName(),
                                        annotationCollector.getParameterAnnotations()
                                );
                            }
                        } catch (IOException e) {
                            System.out.println("Error processing class file: " + entry.getName());
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to read the JAR file: " + jarFilePath + "Exception : " + e.getMessage());
            }

            printAnnotationAnalysis();
        }
    }

    private void printAnnotationAnalysis() {
        // Print the analysis
        System.out.println("JAR Annotation Analysis:");
        if(!classAnnotationMap.isEmpty()) {
            classAnnotationMap.forEach((className, annotationMap) -> {
                System.out.println("**************************************************************************************");
                System.out.println("Class: " + className);
                System.out.println("  Class level annotations : ");
                annotationMap.forEach((annotation, count) ->
                        System.out.println("    " + annotation + ": " + count));

                if(fieldAnnotationMap.get(className) != null) {
                    System.out.println("  Field level annotations : ");
                    fieldAnnotationMap.get(className).forEach((annotation, count) ->
                            System.out.println("    " + annotation + ": " + count));
                }
                if(methodAnnotationMap.get(className) != null) {
                    System.out.println("  Method level annotations : ");
                    methodAnnotationMap.get(className).forEach((annotation, count) ->
                            System.out.println("    " + annotation + ": " + count));
                }
                if(parameterAnnotationMap.get(className) != null) {
                    System.out.println("  Parameter level annotations : ");
                    parameterAnnotationMap.get(className).forEach((annotation, count) ->
                            System.out.println("    " + annotation + ": " + count));
                }
            });
        } else {
            System.out.println("No Classes found with Annotations");
        }
    }
}



