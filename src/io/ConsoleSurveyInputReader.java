package io;

import java.io.IOException;

import io.ConsoleInputReader;

public class ConsoleSurveyInputReader implements SurveyInputReader {
	private InputReader in;

	public ConsoleSurveyInputReader() {
		in = new ConsoleInputReader();
	}

	private String readln() {
		String line;

		try {
			line = in.readln();
		} catch (IOException ex) {
			ex.printStackTrace();
			line = null;
		}

		return line;
	}

	private Integer readInt() {
		Integer num;

		try {
			num = in.readInt();
		} catch (IOException ex) {
			ex.printStackTrace();
			num = null;
		}

		return num;
	}

	public String readMenuChoice() { return readln(); }

	public String readQuestionPrompt() { return readln(); }

	public Integer readQuestionChoiceCount() { return readInt(); }

	public String readQuestionChoice() { return readln(); }

	public String readQuestionResponse() { return readln(); }
}
