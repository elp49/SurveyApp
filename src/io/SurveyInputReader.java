package io;

public interface SurveyInputReader {
	public String readMenuChoice();
	public String readQuestionPrompt();
	public Integer readQuestionChoiceCount();
	public String readQuestionChoice();
	public String readQuestionResponse();
}
