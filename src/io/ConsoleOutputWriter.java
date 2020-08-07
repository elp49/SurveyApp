package io;

import io.OutputWriter;

public class ConsoleOutputWriter implements OutputWriter {

	public ConsoleOutputWriter() { }

	public void print(String str) {
		System.out.print(str);
	}
	
	public void println(String str) {
		System.out.println(str);
	}

	public String getLineSeparator() {
		return System.lineSeparator();
	}
}
