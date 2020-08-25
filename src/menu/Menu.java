package menu;

import survey.SurveyApp;

import java.util.List;

/**
 * A Menu to be displayed to the user to determine their chosen action.
 */
public abstract class Menu {
    public String prompt;
    public List<String> options;

    public void display() { SurveyApp.out.displayMenu(prompt, options); }

    public void display(String[] notes) {
        int i;
        String[] newPrompt = new String[notes.length + 1];

        // Append prompt to first element.
        newPrompt[0] = prompt;

        // Append notes.
        for (i = 1; i <= notes.length; i++)
            newPrompt[i] = notes[i-1];

        // Display menu.
        SurveyApp.out.displayMenu(newPrompt, options);
    }

    public String readValidMenuChoice() { return SurveyApp.in.readValidMenuChoice(options); }
}
