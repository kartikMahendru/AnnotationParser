package org.homeassignment.algorithms.impl;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.homeassignment.algorithms.JarAnnotationParser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class JarAnnotationParserClassGraph implements JarAnnotationParser {

    public void scanJarForAnnotations(String jarPath) {
        try {
            // Create a URLClassLoader to load classes from the JAR file
            URL[] urls = new URL[] { new File(jarPath).toURI().toURL() };
            URLClassLoader classLoader = new URLClassLoader(urls);

            // Use ClassGraph for efficient scanning
            try (ScanResult scanResult = new ClassGraph()
                    .overrideClassLoaders(classLoader)
                    .enableAnnotationInfo()
                    .scan()) {

                for (ClassInfo classInfo : scanResult.getAllClasses()) {
                    System.out.println("Class: " + classInfo.getName());

                    List<String> annotations = new ArrayList<>();
                    for (AnnotationInfo annotationInfo : classInfo.getAnnotationInfo()) {
                        annotations.add(annotationInfo.getName());
                    }

                    if (!annotations.isEmpty()) {
                        System.out.println("  Annotations:");
                        for (String annotation : annotations) {
                            System.out.println("    - " + annotation);
                        }
                    }
                    System.out.println();
                }
            }

        } catch (MalformedURLException e) {
            System.err.println("Invalid JAR file path: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred while scanning Jar File : " + e.getMessage());
        }
    }
}