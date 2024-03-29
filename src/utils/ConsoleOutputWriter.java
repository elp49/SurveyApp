package utils;

public class ConsoleOutputWriter implements OutputWriter {

    public ConsoleOutputWriter() {
    }

    public void print(String s) {
        System.out.print(s);
    }

    public void println(String s) {
        System.out.println(s);
    }

    public String lineSeparator() {
        return System.lineSeparator();
    }
}
