package org.homeassignment;

public class MainApplication {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java AnnotationScanner <path-to-jar>");
            return;
        }
        String jarPath = args[0];
        JarAnnotationParserV2.getInstance().scanJarForAnnotations(jarPath);
    }
}