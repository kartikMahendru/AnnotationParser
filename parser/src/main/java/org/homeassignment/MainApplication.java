package org.homeassignment;

import org.homeassignment.algorithms.JarAnnotationParser;
import org.homeassignment.algorithms.impl.JarAnnotationParserASM;

public class MainApplication {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java AnnotationScanner <path-to-jar>");
            return;
        }
        String jarPath = args[0];

        JarAnnotationParser jarAnnotationParser = new JarAnnotationParserASM();
        jarAnnotationParser.scanJarForAnnotations(jarPath);
    }
}