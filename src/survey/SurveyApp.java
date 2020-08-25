package survey;

import menu.MainMenu;
import survey.io.ConsoleSurveyInputReader;
import survey.io.ConsoleSurveyOutputWriter;
import survey.io.SurveyInputReader;
import survey.io.SurveyOutputWriter;
import utils.Validation;

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
                    survey = new Survey();
                    try {
                        survey.create();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while creating your survey.");
                    }

                    break;

                case MainMenu.DISPLAY_SURVEY:

                    // Test survey for null.
                    if (survey == null) displayNoSurveyLoadedMessage("display");
                    else {
                        try {
                            survey.display();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while displaying your survey.");
                        }
                    }

                    break;

                case MainMenu.LOAD_SURVEY:

                    Survey s = null;

                    try {
                        // Load a survey.
                        s = Survey.load();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while loading your survey.");
                    }

                    // Test survey for null.
                    if (s != null) survey = s;

                    break;

                case MainMenu.SAVE_SURVEY:

                    // Test survey for null.
                    if (survey == null) displayNoSurveyLoadedMessage("save");
                    else {
                        try {
                            survey.save();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while saving your survey.");
                        }
                    }

                    break;

                case MainMenu.TAKE_SURVEY:

                    // Test survey for null.
                    if (survey == null) displayNoSurveyLoadedMessage("take");
                    else {
                        try {
                            survey.take();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while taking your survey.");
                        }
                    }

                    break;

                case MainMenu.MODIFY_SURVEY:

                    // Test survey for null.
                    if (survey == null) displayNoSurveyLoadedMessage("modify");
                    else {
                        try {
                            survey.modify();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while modifying your survey.");
                        }
                    }

                    break;

                case MainMenu.TABULATE_SURVEY:

                    try {
                        // Tabulate a survey.
                        Survey.tabulate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while tabulating your survey.");
                    }

                    break;

                default:

                    break;
            }
        } while (!choice.equals(MainMenu.QUIT));
    }

    public static String getUserMenuChoice(String prompt, List<String> options) {
        String choice;
        boolean isNullOrBlank;

        do {
            // Display menu.
            out.displayMenu(prompt, options);

            // Get user menu choice.
            choice = in.readValidMenuChoice(options);

            // Test for null or empty string.
            if (isNullOrBlank = Validation.isNullOrBlank(choice)) {
                displayInvalidInputMessage("choice");
            }
        } while (isNullOrBlank);

        return choice;
    }

    public static String getUserMenuChoice(String[] prompt, List<String> options) {
        String choice;
        boolean isNullOrBlank;

        do {
            // Display menu.
            out.displayMenu(prompt, options);

            // Get user menu choice.
            choice = in.readValidMenuChoice(options);

            // Test for null or empty string.
            if (isNullOrBlank = Validation.isNullOrBlank(choice)) {
                displayInvalidInputMessage("choice");
            }
        } while (isNullOrBlank);

        return choice;
    }

    public static void displayNoSurveyLoadedMessage(String action) {
        String message;

        if (!Validation.isNullOrBlank(action)) message = "You must have a survey loaded in order to " + action + " it.";
        else message = "You must have a survey loaded.";

        out.displayNote(message);
    }

    public static void displayInvalidInputMessage(String inputType) {
        out.displayNote("You entered an invalid " + inputType + ".");
    }
}
