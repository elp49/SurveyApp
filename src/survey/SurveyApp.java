package survey;

import java.util.ArrayList;
import java.util.List;

import io.SurveyInputReader;
import io.SurveyOutputWriter;
import io.ConsoleSurveyInputReader;
import io.ConsoleSurveyOutputWriter;
import survey.question.QuestionFactory;

public class SurveyApp {
    public static SurveyInputReader in;
    public static SurveyOutputWriter out;
    private Survey survey = null;

    public SurveyApp() { }

    public static void main(String[] args) {
        SurveyApp app = new SurveyApp();
        app.runConsoleApp();
    }

    public void runConsoleApp() {
        in = new ConsoleSurveyInputReader();
        out = new ConsoleSurveyOutputWriter();
        run();
    }

    private void run() {
        String choiceStr;

        // Loop until user quits.
        do {
            // Display main menu.
            out.displayMenu(MainMenu.PROMPT, MainMenu.OPTIONS);

            // Get user menu choice.
            choiceStr = in.readValidMenuChoice(MainMenu.OPTIONS, -1);

            // Perform user action.
			if (!isNullOrEmpty(choiceStr)) {
                switch (choiceStr) {
                    case MainMenu.CREATE:

                        QuestionFactory qf = new QuestionFactory();
                        survey = new Survey(qf);
                        survey.create();

                        break;
                    case MainMenu.DISPLAY:

                        if (survey == null) displayNoSurveyMessage("display");
                        else survey.display();

                        break;
                    case MainMenu.LOAD:

                        survey = Survey.load();

                        break;
                    case MainMenu.SAVE:

                        if (survey == null) displayNoSurveyMessage("save");
                        else survey.save();

                        break;
                    case MainMenu.TAKE:

                        if (survey == null) displayNoSurveyMessage("take");
                        else survey.take();

                        break;
                    case MainMenu.MODIFY:

                        if (survey == null) displayNoSurveyMessage("modify");
                        else survey.modify();

                        break;
                }
			} else {
                displayInvalidInputMessage("choice");
			}
        } while (!choiceStr.equals(MainMenu.QUIT));
    }

    public static void displayNoSurveyMessage(String action) {
        String message;

        if (!isNullOrEmpty(action)) message = "You must have a survey loaded in order to " + action + " it.";
        else message = "You must have a survey loaded.";

        out.displayNote(message);
    }

    public static void displayInvalidInputMessage(String inputType) {
        out.displayNote("You entered an invalid " + inputType + ".");
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static class MainMenu {
        public static final String PROMPT = "Main Menu";
        public static final String CREATE = "Create a new Survey";
        public static final String DISPLAY = "Display an existing Survey";
        public static final String LOAD = "Load an existing Survey";
        public static final String SAVE = "Save the current Survey";
        public static final String TAKE = "Take the current Survey";
        public static final String MODIFY = "Modifying the current Survey";
        public static final String QUIT = "Quit";
        public static final List<String> OPTIONS = new ArrayList<>() {
            {
                add(CREATE);
                add(DISPLAY);
                add(LOAD);
                add(SAVE);
                add(TAKE);
                add(MODIFY);
                add(QUIT);
            }
        };
    }
}
