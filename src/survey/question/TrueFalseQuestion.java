package survey.question;

import survey.SurveyApp;

import java.util.ArrayList;

public class TrueFalseQuestion extends MultipleChoiceQuestion {
    protected final int numChoices = 2;
    protected final int numResponses = 1;
    protected final ChoiceList choiceList = new ChoiceList(new ArrayList<>() {
        {
            add("True");
            add("False");
        }
    });

    public TrueFalseQuestion() {
        prompt = "";
    }

    public ChoiceList getChoiceList() {
        return choiceList;
    }

    @Override
    public String getQuestionType() {
        return "True/False";
    }

    @Override
    public void create() {
        // Get valid prompt.
        prompt = getValidPrompt();
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayQuestionChoiceList(choiceList, true);
    }

    @Override
    public void modify() {
        // Modify the question prompt.
        modifyPrompt();
    }
}
