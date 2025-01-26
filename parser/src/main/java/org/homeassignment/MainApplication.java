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

        //Todo : take input as asm or graph based on which interface is initialised and keep default as asm
        // todo : do testing with non jar files like .txt or zip or add a check for file extension
        JarAnnotationParser jarAnnotationParser = new JarAnnotationParserASM();
        jarAnnotationParser.scanJarForAnnotations(jarPath);
    }
}