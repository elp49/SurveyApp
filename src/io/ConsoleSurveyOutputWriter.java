package io;

import java.util.List;

import io.ConsoleOutputWriter;

public class ConsoleSurveyOutputWriter implements SurveyOutputWriter {
	private OutputWriter out;
	
	public ConsoleSurveyOutputWriter() {
		out = new ConsoleOutputWriter();
	}

	private void println(String str) { out.println(str); }

	public void displayNote(String note) { println(note); }

	public void displayMenu(String prompt, List<String> choices) {
		displayMenuPrompt(prompt);
		displayMenuChoices(choices);
	}

	public void displayMenuPrompt(String prompt) { println(prompt); }

	public void displayMenuChoices(List<String> choices) {
		int i;
		String line;

		for (i = 0; i < choices.size(); i++) {
			line = i+1 + ") " + choices.get(i);
			println(line);
		}
	}

	public void displayAllSurveyNames(List<String> names) {
		int i;
		String line;

		for (i = 0; i < names.size(); i++) {
			line = i+1 + ") " + names.get(i);
			println(line);
		}
	}

	public void displaySurvey(String survey) { println(survey); }

	public void displayQuestion(String question) { println(question); }

	public void displayAllQuestionChoices(String choiceList) { println(choiceList); }
	public void displayAllQuestionChoices(List<String> choiceList) {
		int i;
		String line, choice;
		int numeric = 1;

		for (i = 0; i < choiceList.size(); i++) {
			choice = choiceList.get(i);
			line = numeric + ") " + choice;
			println(line);
		}
	}

	/*public void displayAllQuestionChoices(List<List<String>> choiceSet) {
		int i, j;
		String line, choice;
		int numeric = 1;
		char alpha = 'A';

		int numCols = choiceSet.size();
		int numRows = choiceSet.get(0).size();

		for (i = 0; i < numRows; i++) {
			line = "";
			for (j = 0; j < numCols; j++) {
				if (j % 2 == 0) {
					line += Integer.toString(numeric);
					numeric++;
				} else {
					line += String.valueOf(alpha);
					alpha++;
				}
				choice = choiceSet.get(j).get(i);
				line += ") " + choice + "\t\t";
			}
			println(line);
		}
	}*/

	public void displayQuestionResponse(String response) { println(response); }

	public void displaySurveyResponse(String response) { println(response); }
}
