package org.homeassignment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestUtils {

    // Utility method to capture System.out output during tests
    public static ByteArrayOutputStream captureSystemOut() {
        ByteArrayOutputStream outCapture = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outCapture));
        return outCapture;
    }

    // Utility method to reset System.out after capturing
    public static void resetSystemOut() {
        System.setOut(System.out);
    }
}
