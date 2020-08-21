package survey.io;

import survey.question.ChoiceList;

import java.util.List;

public interface SurveyOutputWriter {
    void displayNote(String note);

    void displayNote(String note, boolean isInline);

    void displayAllNotes(String[] note);

    void displayAllNotes(String[] note, boolean isInline);

    void displayMenu(String prompt, List<String> choices);

    void displayMenu(String[] prompt, List<String> choices);

    void displayMenuPrompt(String prompt);

    void displayMenuPrompt(String[] prompt);

    void displayMenuOptions(List<String> options);

    void displayQuestion(String prompt);

    void displayQuestion(String[] prompt);

    void displayQuestion(String prompt, ChoiceList choiceList);

    void displayQuestion(String[] prompt, ChoiceList choiceList);

    void displayQuestion(String prompt, List<ChoiceList> choiceSet);

    void displayQuestion(String[] prompt, List<ChoiceList> choiceSet);

    void displayQuestionPrompt(String prompt);

    void displayQuestionPrompt(String[] prompt);

    void displayQuestionChoiceList(ChoiceList choiceList);

    void displayQuestionChoiceSet(List<ChoiceList> choiceSet);

    void displaySurveyName(String surveyName);

    void displayQuestionResponse(String response);

    void displaySurveyResponse(String response);
}
