package io;

import java.util.List;

public class ConsoleSurveyOutputWriter implements SurveyOutputWriter {
	private OutputWriter out;
	
	public ConsoleSurveyOutputWriter() {
		out = new ConsoleOutputWriter();
	}

	private void print(String s) { out.print(s); }

	private void println(String s) { out.println(s); }

	private String getLineSeparator() { return out.getLineSeparator(); }

	public void displayNote(String note) {
		println("");
		println(note);
	}

	public void displayMenu(String prompt, List<String> choices) {
		displayMenuPrompt(prompt);
		println("");
		displayMenuOptions(choices);
	}

	public void displayMenuPrompt(String prompt) {
		println("");
		println(prompt);
	}

	public void displayMenuOptions(List<String> options) {
		int i;
		String line;

		for (i = 0; i < options.size(); i++) {
			line = i+1 + ") " + options.get(i);
			println(line);
		}
	}

	//TODO: decide if should remove this method and dynamically create a menu with survey names as options.
	// and then use abody display menu function instead.
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
