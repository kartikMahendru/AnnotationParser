package org.homeassignment.util;

import java.io.File;

public class JarFilePathUtil {

    public static boolean isValidJarFile(String jarFilePath) {
        File jarFile = null;
        try {
            jarFile = new File(jarFilePath);
        }catch (Exception e){
            System.err.println("Error creating File Object from jarFilePath : "+jarFilePath+ " "+ e.getMessage());
            return false;
        }

        if (!jarFile.exists()) {
            System.out.println("Jar File does not exist : "+ jarFilePath);
            return false;
        }

        // Check if the file is a regular file
        if (!jarFile.isFile()) {
            System.out.println("Jar File is not a file : "+ jarFilePath);
            return false;
        }

        // Check if the file is readable
        if (!jarFile.canRead()) {
            System.out.println("No permission to read jarFile : "+ jarFilePath);
            return false;
        }

        // Check if the file has a .jar extension (case insensitive)
        if(!jarFile.getName().toLowerCase().endsWith(".jar")){
            System.out.println("Not a jar file : "+ jarFilePath);
            return false;
        }

        return true;
    }

}
