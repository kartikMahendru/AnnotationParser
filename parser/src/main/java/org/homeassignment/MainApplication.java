package org.homeassignment;

import org.homeassignment.algorithms.JarAnnotationParser;
import org.homeassignment.algorithms.impl.AnnotationLevel;
import org.homeassignment.algorithms.impl.JarAnnotationParserASM;

import java.util.Map;

public class MainApplication {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid number of arguments to the program");
            System.out.println("Usage: java MainApplication <path-to-jar>");
            return;
        }
        String jarPath = args[0];

        // create JarAnnotationParser object and print the response map
        JarAnnotationParser jarAnnotationParser = new JarAnnotationParserASM();
        Map<String, Map<AnnotationLevel, Map<String, Integer>>> responseMap = jarAnnotationParser.scanJarForAnnotations(jarPath);
        jarAnnotationParser.printAnnotationAnalysis(responseMap);
    }
}