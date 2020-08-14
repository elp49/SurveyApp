package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInputReader implements InputReader {
    private final BufferedReader reader;

    public ConsoleInputReader() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String readln() throws IOException {
        return reader.readLine();
    }

    public Integer readInteger() throws IOException {
        String input = readln();
        return parseInteger(input);
    }

    private Integer parseInteger(String s) {
        Integer result = null;

        try {
            result = Integer.parseInt(s);
        } catch (NumberFormatException ignore) {
        }

        return result;
    }
}
