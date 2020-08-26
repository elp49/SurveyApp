package survey;

import menu.MainMenu;
import menu.Menu;
import menu.SurveyMenu;
import menu.TestMenu;
import survey.io.ConsoleSurveyInputReader;
import survey.io.ConsoleSurveyOutputWriter;
import survey.io.SurveyInputReader;
import survey.io.SurveyOutputWriter;
import utils.Validation;

import java.util.List;

public class SurveyApp {
    public static SurveyInputReader in;
    public static SurveyOutputWriter out;
    private String choice;

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
        Menu menu = new MainMenu();

        // Loop until user quits.
        do {
            // Display main menu.
            menu.display();

            // Get user menu choice.
            choice = menu.readValidMenuChoice();

            switch (choice) {
                case MainMenu.SURVEY:
                    try {
                        runSurveyApp();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while running the Survey application.");
                    }

                    break;

                case MainMenu.TEST:
                    try {
                        runTestApp();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while running the Test application.");
                    }

                    break;

                default:
                    break;
            }
        } while (!choice.equals(MainMenu.QUIT));

    }

    private void runSurveyApp() {
        // Create survey object
        Survey survey = null;

        SurveyMenu menu = new SurveyMenu();

        // Loop until user returns to main menu.
        do {
            // Display survey menu.
            menu.display();

            // Get user menu choice.
            choice = menu.readValidMenuChoice();

            switch (choice) {
                case SurveyMenu.CREATE:
                    // Test for null.
                    survey = new Survey();

                    try {
                        survey.create();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while creating your survey.");
                    }

                    break;

                case SurveyMenu.DISPLAY:
                    // Test for null.
                    if (survey == null)
                        displayNoSurveyLoadedMessage("display");

                    else {
                        try {
                            survey.display();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while displaying your survey.");
                        }
                    }

                    break;

                case SurveyMenu.LOAD:
                    Survey s = null;

                    try {
                        // Load a survey.
                        s = Survey.load();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while loading your survey.");
                    }

                    // Test for null.
                    if (s != null) survey = s;

                    break;

                case SurveyMenu.SAVE:
                    // Test survey for null.
                    if (survey == null)
                        displayNoSurveyLoadedMessage("save");

                    else {
                        try {
                            survey.save();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while saving your survey.");
                        }
                    }

                    break;

                case SurveyMenu.TAKE:
                    // Test for null.
                    if (survey == null)
                        displayNoSurveyLoadedMessage("take");

                    else {
                        try {
                            survey.take();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while taking your survey.");
                        }
                    }

                    break;

                case SurveyMenu.MODIFY:
                    // Test for null.
                    if (survey == null)
                        displayNoSurveyLoadedMessage("modify");

                    else {
                        try {
                            survey.modify();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while modifying your survey.");
                        }
                    }

                    break;

                case SurveyMenu.TABULATE:
                    try {
                        // Tabulate a survey.
                        Survey.tabulateSurvey();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while tabulating your survey.");
                    }

                    break;

                default:
                    break;
            }
        } while (!choice.equals(SurveyMenu.RETURN));
    }

    private void runTestApp() {
        // Create test object
        Test test = null;

        TestMenu menu = new TestMenu();

        // Loop until user returns to main menu.
        do {
            // Display test menu.
            menu.display();

            // Get user menu choice.
            choice = menu.readValidMenuChoice();

            switch (choice) {
                case TestMenu.CREATE:
                    // Test for null.
                    test = new Test();

                    try {
                        test.create();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while creating your test.");
                    }

                    break;

                case TestMenu.DISPLAY:
                    // Test for null.
                    if (test == null)
                        displayNoTestLoadedMessage("display");

                    else {
                        try {
                            test.display();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while displaying your test.");
                        }
                    }

                    break;

                case TestMenu.DISPLAY_WITH_ANSWERS:
                    // Test for null.
                    if (test == null)
                        displayNoTestLoadedMessage("display");

                    else {
                        try {
                            test.displayWithAnswers();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while displaying your test.");
                        }
                    }

                    break;

                case TestMenu.LOAD:
                    Test t = null;

                    try {
                        // Load a test.
                        t = Test.load();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while loading your test.");
                    }

                    // Test for null.
                    if (t != null) test = t;

                    break;

                case TestMenu.SAVE:
                    // Test for null.
                    if (test == null)
                        displayNoTestLoadedMessage("save");

                    else {
                        try {
                            test.save();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while saving your test.");
                        }
                    }

                    break;

                case TestMenu.TAKE:
                    // Test for null.
                    if (test == null)
                        displayNoTestLoadedMessage("take");

                    else {
                        try {
                            test.take();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while taking your test.");
                        }
                    }

                    break;

                case TestMenu.MODIFY:
                    // Test for null.
                    if (test == null)
                        displayNoTestLoadedMessage("modify");

                    else {
                        try {
                            test.modify();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.displayNote("There was an error while modifying your test.");
                        }
                    }

                    break;

                case TestMenu.TABULATE:
                    try {
                        // Tabulate a test.
                        Test.tabulateTest();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while tabulating your test.");
                    }

                    break;

                case TestMenu.GRADE:
                    try {
                        // Grade a test.
                        Test.grade();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.displayNote("There was an error while tabulating your test.");
                    }

                    break;

                default:
                    break;
            }
        } while (!choice.equals(TestMenu.RETURN));
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
        if (!Validation.isNullOrBlank(action))
            out.displayNote("You must have a survey loaded in order to " + action + " it.");
        else
            out.displayNote("You must have a survey loaded.");
    }

    public static void displayNoTestLoadedMessage(String action) {
        if (!Validation.isNullOrBlank(action))
            out.displayNote("You must have a test loaded in order to " + action + " it.");
        else
            out.displayNote("You must have a test loaded.");
    }

    public static void displayInvalidInputMessage(String inputType) {
        out.displayNote("You entered an invalid " + inputType + ".");
    }
}
