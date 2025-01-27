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

    // declare maps for different type of annotations
    private final Map<String, Map<String, Integer>> classAnnotationMap = new HashMap<>();
    private final Map<String, Map<String, Integer>> fieldAnnotationMap = new HashMap<>();
    private final Map<String, Map<String, Integer>> methodAnnotationMap = new HashMap<>();
    private final Map<String, Map<String, Integer>> parameterAnnotationMap = new HashMap<>();

    public  Map<String, Map<AnnotationLevel, Map<String, Integer>>> scanJarForAnnotations(String jarFilePath) {

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

            return prepareResponseMap();
        }
        return null;
    }

    private Map<String, Map<AnnotationLevel, Map<String, Integer>>> prepareResponseMap() {
        /*
        collect all maps and create a response map
        Class Name -> ( Annotation Level  -> (Annotation Name : Count ) )
        for each class, for each annotation level in that class, store annotation name to count map
         */
        Map<String, Map<AnnotationLevel, Map<String, Integer>>> responseMap = new HashMap<>();
        if(!classAnnotationMap.isEmpty()) {
            classAnnotationMap.forEach((className, annotationMap) -> {
                responseMap.putIfAbsent(className, new HashMap<>());
                Map<AnnotationLevel, Map<String, Integer>> internalMap = responseMap.get(className);
                internalMap.put(AnnotationLevel.CLASS, annotationMap);
            });
        }

        if(!fieldAnnotationMap.isEmpty()) {
            fieldAnnotationMap.forEach((className, annotationMap) -> {
                responseMap.putIfAbsent(className, new HashMap<>());
                Map<AnnotationLevel, Map<String, Integer>> internalMap = responseMap.get(className);
                internalMap.put(AnnotationLevel.FIELD, annotationMap);
            });
        }

        if(!methodAnnotationMap.isEmpty()) {
            methodAnnotationMap.forEach((className, annotationMap) -> {
                responseMap.putIfAbsent(className, new HashMap<>());
                Map<AnnotationLevel, Map<String, Integer>> internalMap = responseMap.get(className);
                internalMap.put(AnnotationLevel.METHOD, annotationMap);
            });
        }

        if(!parameterAnnotationMap.isEmpty()) {
            parameterAnnotationMap.forEach((className, annotationMap) -> {
                responseMap.putIfAbsent(className, new HashMap<>());
                Map<AnnotationLevel, Map<String, Integer>> internalMap = responseMap.get(className);
                internalMap.put(AnnotationLevel.PARAMETER, annotationMap);
            });
        }

        return responseMap;
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



