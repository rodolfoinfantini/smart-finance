package main.input;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Keyboard {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String readLine() {
        try {
            return reader.readLine();
        } catch (final Exception e) {
            return null;
        }
    }
}
