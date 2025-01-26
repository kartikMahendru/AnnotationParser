package org.homeassignment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

@Nonnull
public class JarAnnotationParserV2 {

    private static JarAnnotationParserV2 instance = null;

    private JarAnnotationParserV2(){}

    public static synchronized JarAnnotationParserV2 getInstance()
    {
        if (instance == null)
            instance = new JarAnnotationParserV2();

        return instance;
    }

    public void scanJarForAnnotations(String jarPath) {
        // Create a URLClassLoader with the specified JAR file
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            System.out.println("JAR file does not exist: " + jarPath);
            return;
        }

        URL jarURL = null;
        try {
            jarURL = jarFile.toURI().toURL();
        } catch (MalformedURLException e) {
            System.out.println("jar path is invalid/wrongly formatted:  "+ e.getMessage());
            return;
        }
        URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});

        // Build the Reflections object to scan the JAR
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader(classLoader))
                .setScanners(Scanners.TypesAnnotated, Scanners.MethodsAnnotated, Scanners.FieldsAnnotated, Scanners.ConstructorsAnnotated));

        // Scan for classes annotated with any annotation
        Set<String> annotatedClasses = reflections.getAll(Scanners.TypesAnnotated);
//        for (Class<?> clazz : annotatedClasses) {
//            checkAnnotations(clazz);
//        }

        // Scan for methods annotated with any annotation
        Set<Method> annotatedMethods = reflections.getMethodsAnnotatedWith(Annotation.class);
        for (Method method : annotatedMethods) {
            checkAnnotations(method);

            // Manually check for annotations on constructor parameters
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                checkAnnotations(parameter);
            }
        }

        // Scan for fields annotated with any annotation
        Set<Field> annotatedFields = reflections.getFieldsAnnotatedWith(Annotation.class);
        for (Field field : annotatedFields) {
            checkAnnotations(field);
        }

        // Scan for constructors annotated with any annotation
        Set<Constructor> annotatedConstructors = reflections.getConstructorsAnnotatedWith(Annotation.class);
        for (Constructor<?> constructor : annotatedConstructors) {
            checkAnnotations(constructor);

            // Manually check for annotations on constructor parameters
            Parameter[] parameters = constructor.getParameters();
            for (Parameter parameter : parameters) {
                checkAnnotations(parameter);
            }
        }
    }

    private static void checkAnnotations(Object element) {
        Annotation[] annotations = null;

        if (element instanceof Class) {
            annotations = ((Class<?>) element).getAnnotations();
            if (annotations.length > 0) {
                System.out.println("Class: " + ((Class<?>) element).getName());
            }
        } else if (element instanceof Method) {
            annotations = ((Method) element).getAnnotations();
            if (annotations.length > 0) {
                System.out.println("Method: " + ((Method) element).getName());
            }
        } else if (element instanceof Field) {
            annotations = ((Field) element).getAnnotations();
            if (annotations.length > 0) {
                System.out.println("Field: " + ((Field) element).getName());
            }
        } else if (element instanceof Constructor) {
            annotations = ((Constructor<?>) element).getAnnotations();
            if (annotations.length > 0) {
                System.out.println("Constructor: " + ((Constructor<?>) element).getName());
            }
        } else if (element instanceof Parameter) {
            annotations = ((Parameter) element).getAnnotations();
            if (annotations.length > 0) {
                System.out.println("Parameter: " + ((Parameter) element).getName());
            }
        }

        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                System.out.println("  Annotated with: " + annotation.annotationType().getName());
            }
        }
    }
}

