package survey.question;

import survey.SurveyApp;

import java.util.ArrayList;

public class TrueFalseQuestion extends MultipleChoiceQuestion {

    public TrueFalseQuestion() {
        prompt = "";
        numResponses = 1;
        questionType = "True/False";
        responseType = "choice";
        numChoices = 2;
        choiceList = new ChoiceList(new ArrayList<>() {
            {
                add("True");
                add("False");
            }
        });
    }

    @Override
    public void create() {
        // Get valid prompt.
        prompt = getValidPrompt();
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestion(prompt, choiceList);
    }

    @Override
    public void modify() {
        // Modify the question prompt.
        modifyPrompt();
    }
}
