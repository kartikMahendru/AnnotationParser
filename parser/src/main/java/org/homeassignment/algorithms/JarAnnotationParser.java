package org.homeassignment.algorithms;

import org.homeassignment.algorithms.impl.AnnotationLevel;

import java.util.Map;

// interface for flexibility
public interface JarAnnotationParser {
     Map<String, Map<AnnotationLevel, Map<String, Integer>>> scanJarForAnnotations(String path);
     void printAnnotationAnalysis(Map<String, Map<AnnotationLevel, Map<String, Integer>>> responseMap);
}
