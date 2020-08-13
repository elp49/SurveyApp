package survey;

import survey.io.ConsoleSurveyInputReader;
import survey.io.ConsoleSurveyOutputWriter;
import survey.io.SurveyInputReader;
import survey.io.SurveyOutputWriter;

import java.io.IOException;
import java.util.ArrayList;
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
                        try {
                            survey.create();
                        } catch (Exception ignore) {
                            continue;
                        }

                        break;

                    case MainMenu.DISPLAY:

                        if (survey == null) displayNoSurveyLoadedMessage("display");
                        else {
                            try {
                                survey.display();
                            } catch (Exception ignore) {
                                continue;
                            }
                        }

                        break;

                    case MainMenu.LOAD:

                        String surveyName;

                        // Get all survey names.
                        List<String> allSurveyNames = Survey.getAllSurveyNames();

                        // Get all survey file paths.
                        List<String> allSurveyPaths = Survey.getAllSurveyFilePaths();

                        // Display survey menu.
                        out.displayMenu("Please select a file to load:", allSurveyNames);

                        // Get user menu choice.
                        surveyName = in.readValidMenuChoice(allSurveyNames, -1);

                        // Get index of survey name.
                        int index = allSurveyNames.indexOf(surveyName);

                        // Get survey file path.
                        String surveyPath = allSurveyPaths.get(index);


                        try {
                            survey = Survey.deserialize(surveyPath);
                        } catch (IOException | ClassNotFoundException ignore) {
                            String choice;
                            String DELETE = "Yes, delete it.";
                            String KEEP = "No, keep it.";
                            List<String> options = new ArrayList<>() {
                                {
                                    add(DELETE);
                                    add(KEEP);
                                }
                            };
                            out.displayNote("This serialized survey file have become corrupted. This is likely because the Survey App has been updated since this file was saved.");
                            out.displayMenu("Would you like to delete it now?", options);
                            choice = in.readValidMenuChoice(options, -1);

                            // TODO: implement deleting survey from survey class.
                            if (choice.equals(DELETE)) {
                                boolean wasDeleted;
                                //throws IllegalArgumentException, SecurityException
                                try {
                                    wasDeleted = Survey.delete(surveyPath);
                                    if (wasDeleted) out.displayNote("Successfully deleted.");
                                    else out.displayNote("Deletion was unsuccessful. :,(");
                                } catch (IllegalArgumentException e) {
                                    out.displayNote("Oops, that file does not exist. This could be an issue relating to the file path.");
                                } catch (SecurityException e) {
                                    out.displayNote("STOP! You do not have the security permissions to delete this file. This incident will be reported to Donald Trump.");
                                }


                            }
                        }
                        break;

                    case MainMenu.SAVE:

                        if (survey == null) displayNoSurveyLoadedMessage("save");
                        else {
                            try {
                                Survey.serialize(survey);
                                out.displayNote("Saved successfully.");
                            } catch (Exception e) {
                                e.printStackTrace();
                                out.displayNote("There was an error while saving your survey.");
                            }
                        }

                        break;

                    case MainMenu.TAKE:

                        if (survey == null) displayNoSurveyLoadedMessage("take");
                        else {
                            try {
                                survey.take();
                            } catch (Exception ignore) {
                                continue;
                            }
                        }

                        break;

                    case MainMenu.MODIFY:

                        if (survey == null) displayNoSurveyLoadedMessage("modify");
                        else {
                            try {
                                survey.modify();
                            } catch (Exception ignore) {
                                continue;
                            }
                        }

                        break;
                }
            } else {
                displayInvalidInputMessage("choice");
            }
        } while (!choiceStr.equals(MainMenu.QUIT));
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

    /**
     * Create a name for the serialized file on disk.
     *
     * @param survey The survey to be serialized.
     * @return The serialized survey filename.
     */
    protected static String createFilename(Survey survey) {
        // Create survey filename.
        String filename = "Survey";
        return filename;
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
