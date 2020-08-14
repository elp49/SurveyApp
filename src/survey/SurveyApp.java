package survey;

import menu.MainMenu;
import survey.io.ConsoleSurveyInputReader;
import survey.io.ConsoleSurveyOutputWriter;
import survey.io.SurveyInputReader;
import survey.io.SurveyOutputWriter;

import java.util.List;

public class SurveyApp {
    public static SurveyInputReader in;
    public static SurveyOutputWriter out;
    private Survey survey = null;

    public SurveyApp() {
    }

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
        String choice;

        // Loop until user quits.
        do {
            // Get user menu choice.
            choice = getUserMenuChoice(MainMenu.PROMPT, MainMenu.OPTIONS);

            switch (choice) {
                case MainMenu.CREATE_SURVEY:

                    // Test survey for null.
                    survey = new Survey(new QuestionFactory());
                    survey.create();

                    break;

                case MainMenu.DISPLAY_SURVEY:

                    // Test survey for null.
                    if (survey == null) displayNoSurveyLoadedMessage("display");
                    else survey.display();

                    break;

                case MainMenu.LOAD_SURVEY:

                    // Load a survey.
                    Survey s = Survey.load();

                    // Test survey for null.
                    if (s != null) survey = s;

                    break;

                case MainMenu.SAVE_SURVEY:

                    // Test survey for null.
                    if (survey == null) displayNoSurveyLoadedMessage("save");
                    else survey.save();

                    break;

                case MainMenu.TAKE_SURVEY:

                    // Test survey for null.
                    if (survey == null) displayNoSurveyLoadedMessage("take");
                    else survey.take();

                    break;

                case MainMenu.MODIFY_SURVEY:

                    // Test survey for null.
                    if (survey == null) displayNoSurveyLoadedMessage("modify");
                    else survey.modify();

                    break;

                default:

                    break;
            }
        } while (!choice.equals(MainMenu.QUIT));
    }

    public static String getUserMenuChoice(String prompt, List<String> options) {
        String choice;
        boolean isNullOrEmpty;

        do {
            // Display menu.
            out.displayMenu(prompt, options);

            // Get user menu choice.
            choice = in.readValidMenuChoice(options, -1);

            // Test for null or empty string.
            if (isNullOrEmpty = isNullOrEmpty(choice)) {
                displayInvalidInputMessage("choice");
            }
        } while (isNullOrEmpty);

        return choice;
    }

    public static void displayNoSurveyLoadedMessage(String action) {
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
}
