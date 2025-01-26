package org.homeassignment;

import org.objectweb.asm.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Collects annotations from class files and methods using ASM library.
 */
public class AnnotationCollector extends ClassVisitor {
    private String className;
    private final Map<String, Integer> annotations = new HashMap<>();

    public AnnotationCollector() {
        super(Opcodes.ASM9);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name.replace('/', '.'); // Convert class name from '/' to '.' format
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        // Extract annotation type and count occurrences
        String annotationName = Type.getType(descriptor).getClassName();
        annotations.put(annotationName, annotations.getOrDefault(annotationName, 0) + 1);
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        // Detect and collect method-level annotations
        return new MethodVisitor(Opcodes.ASM9) {
            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                String annotationName = Type.getType(descriptor).getClassName();
                annotations.put(annotationName, annotations.getOrDefault(annotationName, 0) + 1);
                return super.visitAnnotation(descriptor, visible);
            }
        };
    }

    public String getClassName() {
        return className;
    }

    public Map<String, Integer> getAnnotations() {
        return annotations;
    }
}