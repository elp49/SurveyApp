package survey.question;

import survey.SurveyApp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChoiceList implements Serializable {
    private static long serialVersionUID = 1L;
    private List<String> choices;

    public ChoiceList() { choices = new ArrayList<>(); }

    public ChoiceList(List<String> choices) {
        this.choices = choices;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void display() {
        SurveyApp.out.displayAllQuestionChoices(choices);
    }

    public int size() { return choices.size(); }

    public boolean add(String choice) {
        return choices.add(choice);
    }
}
