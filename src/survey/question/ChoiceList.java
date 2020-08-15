package survey.question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChoiceList implements Serializable {
    private static long serialVersionUID = 1L;
    private final List<String> choices;

    public ChoiceList() {
        choices = new ArrayList<>();
    }

    public ChoiceList(List<String> choices) {
        this.choices = choices;
    }

    public List<String> getChoices() {
        return choices;
    }

    public int size() {
        return choices.size();
    }

    public boolean add(String choice) {
        return choices.add(choice);
    }

    public String get(int i) {
        return choices.get(i);
    }

    public String set(int index, String choice) {
        return choices.set(index, choice);
    }
}
