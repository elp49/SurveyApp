package io;

import java.util.List;

public interface SurveyOutputWriter {
	void displayNote(String note);
	void displayMenu(String prompt, List<String> choices);
	void displayMenuPrompt(String prompt);
	void displayMenuOptions(List<String> options);
	void displayAllSurveyNames(List<String> names);
	void displaySurvey(String survey);
	void displayQuestion(String question);
	void displayAllQuestionChoices(String choiceList);
	void displayAllQuestionChoices(List<String> choiceSet);
	void displayQuestionResponse(String response);
	void displaySurveyResponse(String response);
}
