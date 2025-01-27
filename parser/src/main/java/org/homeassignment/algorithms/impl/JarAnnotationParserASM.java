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

    public  Map<String, Map<AnnotationLevel, Map<String, Integer>>> scanJarForAnnotations(String jarFilePath) {

        // declare mas for different type of annotations
        // map structure : <Class Name <Annotation Level, <Annotation Name, Annotation Count>>>
        Map<String, Map<AnnotationLevel, Map<String, Integer>>> annotationMap = new HashMap<>();

        // check for validity of jar file
        if(JarFilePathUtil.isValidJarFile(jarFilePath)) {

            try (JarFile jarFile = new JarFile(new File(jarFilePath))) {
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    // parse each entry ending with .class (complied java classes)
                    if (entry.getName().endsWith(".class")) {
                        try (InputStream inputStream = jarFile.getInputStream(entry)) {
                            ClassReader classReader = new ClassReader(inputStream);
                            // initialize annotation collector to visit all annotations in a class file
                            AnnotationCollector annotationCollector = new AnnotationCollector();
                            classReader.accept(annotationCollector, 0);

                            // preparing all the annotation maps structure : (Class Name : (Annotation Name, count of annotation))
                            if (!annotationCollector.getClassAnnotations().isEmpty()) {
                                annotationMap.putIfAbsent(annotationCollector.getClassName(), new HashMap<>());
                                Map<AnnotationLevel, Map<String, Integer>> internalMap = annotationMap.get(annotationCollector.getClassName());
                                internalMap.put(AnnotationLevel.CLASS, annotationCollector.getClassAnnotations());
                            }

                            if (!annotationCollector.getFieldAnnotations().isEmpty()) {
                                annotationMap.putIfAbsent(annotationCollector.getClassName(), new HashMap<>());
                                Map<AnnotationLevel, Map<String, Integer>> internalMap = annotationMap.get(annotationCollector.getClassName());
                                internalMap.put(AnnotationLevel.FIELD, annotationCollector.getFieldAnnotations());
                            }

                            if (!annotationCollector.getMethodAnnotations().isEmpty()) {
                                annotationMap.putIfAbsent(annotationCollector.getClassName(), new HashMap<>());
                                Map<AnnotationLevel, Map<String, Integer>> internalMap = annotationMap.get(annotationCollector.getClassName());
                                internalMap.put(AnnotationLevel.METHOD, annotationCollector.getMethodAnnotations());
                            }

                            if (!annotationCollector.getParameterAnnotations().isEmpty()) {
                                annotationMap.putIfAbsent(annotationCollector.getClassName(), new HashMap<>());
                                Map<AnnotationLevel, Map<String, Integer>> internalMap = annotationMap.get(annotationCollector.getClassName());
                                internalMap.put(AnnotationLevel.PARAMETER, annotationCollector.getParameterAnnotations());
                            }
                        } catch (IOException e) {
                            System.out.println("Error processing class file: " + entry.getName());
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to read the JAR file: " + jarFilePath + "Exception : " + e.getMessage());
            }

            return annotationMap;
        }
        return null;
    }

    public void printAnnotationAnalysis(Map<String, Map<AnnotationLevel, Map<String, Integer>>> responseMap) {
        // Print the analysis
        System.out.println("JAR Annotation Analysis:");
        if(responseMap != null && !responseMap.isEmpty()) {
            responseMap.forEach((className, annotationMap) -> {
                System.out.println("**************************************************************************************");
                System.out.println("Class: " + className);

                if(annotationMap.get(AnnotationLevel.CLASS) != null) {
                    System.out.println("  Class level annotations : ");
                    annotationMap.get(AnnotationLevel.CLASS).forEach((annotation, count) ->
                            System.out.println("    " + annotation + ": " + count));
                }

                if(annotationMap.get(AnnotationLevel.FIELD) != null) {
                    System.out.println("  Field level annotations : ");
                    annotationMap.get(AnnotationLevel.FIELD).forEach((annotation, count) ->
                            System.out.println("    " + annotation + ": " + count));
                }

                if(annotationMap.get(AnnotationLevel.METHOD) != null) {
                    System.out.println("  Method level annotations : ");
                    annotationMap.get(AnnotationLevel.METHOD).forEach((annotation, count) ->
                            System.out.println("    " + annotation + ": " + count));
                }

                if(annotationMap.get(AnnotationLevel.PARAMETER) != null) {
                    System.out.println("  Parameter level annotations : ");
                    annotationMap.get(AnnotationLevel.PARAMETER).forEach((annotation, count) ->
                            System.out.println("    " + annotation + ": " + count));
                }
            });
        } else if(responseMap == null) {
            System.out.println("Error while analyzing Jar file validity");
        } else {
            System.out.println("No Classes found with Annotations");
        }
    }
}



