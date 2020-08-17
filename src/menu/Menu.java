package menu;

import survey.SurveyApp;
import utils.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * A Menu to be displayed to the user to determine their chosen action.
 */
public abstract class Menu {
    public String prompt;
    public List<String> options;
    public static final String RETURN = "Return to previous menu";

    Menu() {
        prompt = "Unknown Menu";
        options = new ArrayList<>();
    }

    Menu(String prompt, List<String> options) {
        this.prompt = prompt;
        this.options = options;
        this.options.add(RETURN);
    }

    public void display() {
        SurveyApp.out.displayMenuPrompt(prompt);
        SurveyApp.out.displayMenuOptions(options);
    }

    public void display(String[] notes) {
        SurveyApp.out.displayMenuPrompt(prompt);
        SurveyApp.out.displayAllNotes(notes, true);
        SurveyApp.out.displayMenuOptions(options);
    }

    public String readValidMenuChoice() {
        Integer choiceNum;
        String choiceStr = "";

        if (!Validation.isNullOrEmpty(options)) {
            // Get user menu choice.
            choiceNum = SurveyApp.in.readMenuChoice();
            if (choiceNum != null) {
                try {
                    // Get choice string.
                    choiceStr = options.get(choiceNum - 1);
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }

        return choiceStr;
    }
}
