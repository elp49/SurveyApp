package survey.io;

import survey.question.ChoiceList;

import java.util.List;

public interface SurveyOutputWriter {
    void displayNote(String note);

    void displayNote(String note, boolean isInline);

    void displayNote(String[] note);

    void displayNote(String[] note, boolean isInline);

    void displayMenu(String prompt, List<String> choices);

    void displayMenuPrompt(String prompt);

    void displayMenuOptions(List<String> options);

    void displayAllSurveyNames(List<String> names);

    void displaySurvey(String survey);

    void displayQuestionPrompt(String prompt);

    void displayQuestionChoiceList(ChoiceList choiceList);

    void displayQuestionChoiceList(ChoiceList choiceList, boolean isInline);

    void displayQuestionChoiceSet(List<ChoiceList> choiceSet);

    void displayQuestionChoiceSet(List<ChoiceList> choiceSet, boolean isInline);

    void displayQuestionResponse(String response);

    void displaySurveyResponse(String response);
}
