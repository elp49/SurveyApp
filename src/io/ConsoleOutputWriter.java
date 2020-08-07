package io;

public class ConsoleOutputWriter implements OutputWriter {

	public ConsoleOutputWriter() { }

	public void print(String s) {
		System.out.print(s);
	}
	
	public void println(String s) {
		System.out.println(s);
	}

	public String getLineSeparator() {
		return System.lineSeparator();
	}
}
