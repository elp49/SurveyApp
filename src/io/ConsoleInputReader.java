package io;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.NumberFormatException;

import io.InputReader;

public class ConsoleInputReader implements InputReader {
	private BufferedReader reader;

	public ConsoleInputReader() { reader = new BufferedReader(new InputStreamReader(System.in)); }

	public String readln() throws IOException { return reader.readLine(); }

	public Integer readInt() throws IOException {
		String input = readln();
		return parseInt(input);
	}

	private Integer parseInt(String str) {
		int num;
		Integer result;

		try {
			num = Integer.parseInt(str);
			result = new Integer(num);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			result = null;
		}

		return result;
	}
}
