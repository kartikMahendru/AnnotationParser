package org.homeassignment;

import org.homeassignment.algorithms.JarAnnotationParser;
import org.homeassignment.algorithms.impl.JarAnnotationParserASM;
import org.homeassignment.algorithms.impl.JarAnnotationParserClassGraph;
import org.homeassignment.util.JarFilePathUtil;

public class MainApplication {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java AnnotationScanner <path-to-jar>");
            return;
        }
        String jarPath = args[0];

        if(JarFilePathUtil.isValidJarFile(jarPath)) {
            JarAnnotationParser jarAnnotationParser = new JarAnnotationParserASM();
            jarAnnotationParser.scanJarForAnnotations(jarPath);
        }
    }
}