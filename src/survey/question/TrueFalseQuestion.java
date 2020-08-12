package survey.question;

import java.util.ArrayList;

public class TrueFalseQuestion extends MultipleChoiceQuestion {
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
}
