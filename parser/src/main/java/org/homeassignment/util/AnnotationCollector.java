package org.homeassignment.util;

import org.objectweb.asm.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Collects annotations from class files and methods using ASM library.
 */
public class AnnotationCollector extends ClassVisitor {
    private String className;
    private final Map<String, Integer> classAnnotations = new HashMap<>();
    private final Map<String, Integer> fieldAnnotations = new HashMap<>();
    private final Map<String, Integer> methodAnnotations = new HashMap<>();
    private final Map<String, Integer> parameterAnnotations = new HashMap<>();

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
        classAnnotations.put(annotationName, classAnnotations.getOrDefault(annotationName, 0) + 1);
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return new FieldVisitor(Opcodes.ASM9) {
            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                String annotationName = Type.getType(descriptor).getClassName();
                fieldAnnotations.put(annotationName,  fieldAnnotations.getOrDefault(annotationName, 0) + 1);
                return super.visitAnnotation(descriptor, visible);
            }
        };
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        // Detect and collect method-level annotations
        return new MethodVisitor(Opcodes.ASM9) {
            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                String annotationName = Type.getType(descriptor).getClassName();
                methodAnnotations.put(annotationName, methodAnnotations.getOrDefault(annotationName, 0) + 1);
                return super.visitAnnotation(descriptor, visible);
            }

            @Override
            public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
                String annotationName = Type.getType(descriptor).getClassName();
                parameterAnnotations.put(annotationName, parameterAnnotations.getOrDefault(annotationName, 0) + 1);
                return super.visitParameterAnnotation(parameter, descriptor, visible);
            }
        };
    }

    public String getClassName() {
        return className;
    }

    public Map<String, Integer> getClassAnnotations() {
        return classAnnotations;
    }

    public Map<String, Integer> getFieldAnnotations() {
        return fieldAnnotations;
    }

    public Map<String, Integer> getMethodAnnotations() {
        return methodAnnotations;
    }

    public Map<String, Integer> getParameterAnnotations() {
        return parameterAnnotations;
    }
}