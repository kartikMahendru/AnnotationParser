package org.homeassignment.util;

import java.io.File;

public class JarFilePathUtil {

    public static boolean isValidJarFile(String jarFilePath) {
        File jarFile = new File(jarFilePath);

        // Check if the file exists
        if (!jarFile.exists()) {
            return false;
        }

        // Check if the file is a regular file
        if (!jarFile.isFile()) {
            return false;
        }

        // Check if the file is readable
        if (!jarFile.canRead()) {
            return false;
        }

        // Check if the file has a .jar extension (case insensitive)
        return jarFile.getName().toLowerCase().endsWith(".jar");
    }

}
