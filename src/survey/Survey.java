package survey;

import menu.CreateQuestionMenu;
import menu.DeleteMenu;
import menu.Menu;
import survey.question.Question;
import survey.response.QuestionResponse;
import survey.response.SurveyResponse;
import utils.FileConfiguration;
import utils.FileUtils;
import utils.SerializationHelper;
import utils.Validation;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Survey implements Serializable {
    protected static long serialVersionUID = 1L;
    protected static final String basePath = FileConfiguration.SERIALIZED_FILES_DIRECTORY + "Survey" + File.separator;
    protected String name;
    public final static String NAME_SEPARATOR = "-";
    protected final QuestionFactory questionFactory;
    protected final List<Question> questionList;
    protected transient SurveyResponse surveyResponse;

    public Survey(QuestionFactory questionFactory) {
        this.questionFactory = questionFactory;
        questionList = new ArrayList<>();
        surveyResponse = null;
    }

    public String getName() {
        return name;
    }

    public void create() {
        String choice;
        Question q;

        // Create survey name.
        name = createSurveyName();

        // Loop until user quits.
        do {
            // Get user choice from question menu.
            choice = SurveyApp.getUserMenuChoice(CreateQuestionMenu.PROMPT, CreateQuestionMenu.OPTIONS);

            if (!choice.equals(CreateQuestionMenu.RETURN)) {
                // Use survey.question factory to get new survey.question.
                q = questionFactory.getQuestion(choice);

                try {
                    // Create survey.question specific attributes.
                    q.create();
                } catch (NullPointerException ignore) {
                    SurveyApp.out.displayNote("That type of question cannot be created right now. Please try another.");
                    q = null;
                }

                if (q != null) {
                    // Add survey.question to survey.question list.
                    questionList.add(q);

                    SurveyApp.out.displayNote("Successfully added new " + q.getQuestionType() + " question.");
                }
            }
        } while (!choice.equals(CreateQuestionMenu.RETURN));
    }

    public void display() {
        // Display survey name.
        displayName();

        // Display each question in survey.
        for (Question q : questionList)
            q.display();
    }

    protected void displayName() {
        SurveyApp.out.displaySurveyName(name);
    }

    /**
     * Get the path of a survey to load and try to deserialize it.
     *
     * @return The deserialized survey or null if no surveys can be loaded.
     */
    public static Survey load() {
        String surveyPath;
        boolean isNullOrBlank;
        boolean isCorrupted = false;
        Survey result = null;

        do {
            surveyPath = getSurveyPath();
            if (!(isNullOrBlank = Validation.isNullOrBlank(surveyPath))) {
                try {
                    result = Survey.deserialize(surveyPath);

                    SurveyApp.out.displayNote("Loaded successfully.");

                    isCorrupted = false;
                } catch (IOException | ClassNotFoundException ignore) {
                    // Survey file is likely out of sync with survey class.
                    SurveyApp.out.displayAllNotes(new String[]{
                            "This serialized survey file have become corrupted.",
                            "This is likely because SurveyApp has been updated since this file was saved."
                    });

                    isCorrupted = true;

                    handleCorruptedSurvey(surveyPath);
                }
            }

            // If the user has chosen a non-corrupted survey to load
            // or if they have no surveys to load, then quit.
            // If a survey was found to be corrupted, then continue.
        } while (isCorrupted && !isNullOrBlank);

        return result;
    }

    public void save() {
        try {
            // Try to serialize survey to file on disk.
            Survey.serialize(this);
            SurveyApp.out.displayNote("Saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            SurveyApp.out.displayNote("There was an error while saving your survey.");
        }
    }

    public void take() {
        QuestionResponse qr;

        // Initialize survey response object.
        surveyResponse = new SurveyResponse(name);

        // Display survey name.
        displayName();

        // Loop through each question.
        for (Question q : questionList) {
            // Display question.
            q.display();

            // Read user question response.
            qr = q.readQuestionResponse();

            // Add question response to survey response.
            surveyResponse.add(qr);
        }

        // Save response.
        surveyResponse.save();

        // Clean up survey response object.
        surveyResponse = null;
    }

    public void modify() {
        String choice;
        boolean isReturn;
        int index;
        List<String> options;

        if (!questionList.isEmpty()) {
            do {
                // Initialize options list.
                options = new ArrayList<>();

                // Add each question's type to options list.
                for (Question q : questionList)
                    options.add("Type: " + q.getQuestionType() + ",  Prompt: " + q.getPrompt());

                // Append return option.
                options.add(Menu.RETURN);

                // Get user chosen question.
                choice = SurveyApp.getUserMenuChoice(new String[]{
                        "Which question do you wish to modify?",
                        "Below is the list of questions you have created."
                }, options);

                if (!(isReturn = choice.equals(Menu.RETURN))) {
                    // Get index of question to be modified.
                    index = options.indexOf(choice);

                    // Modify question.
                    questionList.get(index).modify();
                }
            } while (!isReturn);
        } else {
            SurveyApp.out.displayNote("Your survey does not have any questions to modify yet.");
        }
    }

    /**
     * Prompt the user to choose one of the available surveys and get its path.
     *
     * @return The path to the chosen survey or null if no surveys are available.
     */
    protected static String getSurveyPath() {
        String choice;
        String result = null;
        List<String> allSurveyPaths = null;

        try {
            // Try to get all survey file paths.
            allSurveyPaths = Survey.getAllSurveyFilePaths();
        } catch (IllegalStateException ignore) {
            SurveyApp.out.displayNote("You have not saved any surveys yet.");
        }

        // Test survey path list is not null or empty.
        if (!Validation.isNullOrEmpty(allSurveyPaths)) {
            // Add the option to return to previous menu.
            allSurveyPaths.add(Menu.RETURN);

            // Get all survey filenames.
            List<String> allSurveyNames = FileUtils.parseAllFilenames(allSurveyPaths);

            // Get user chosen survey.
            choice = SurveyApp.getUserMenuChoice("Please select a file to load:", allSurveyNames);

            if (!choice.equals(Menu.RETURN)) {
                // Get survey path from list.
                result = allSurveyPaths.get(allSurveyNames.indexOf(choice));
            }
        }

        return result;
    }

    public static void handleCorruptedSurvey(String surveyPath) {
        // Get user choice from delete menu.
        String choice = SurveyApp.getUserMenuChoice(DeleteMenu.PROMPT, DeleteMenu.OPTIONS);

        if (choice.equals(DeleteMenu.DELETE)) {
            try {
                // Try to delete survey.
                if (Survey.delete(surveyPath)) SurveyApp.out.displayNote("Successfully deleted.");
                else SurveyApp.out.displayNote("Deletion was unsuccessful. :,(");
            } catch (IllegalArgumentException e) {
                SurveyApp.out.displayAllNotes(new String[]{
                        "Oops, that file does not exist.",
                        "This could be an issue relating to the file path."
                });
            } catch (SecurityException e) {
                SurveyApp.out.displayNote("You do not have the security permissions to delete this file.");
            }
        }
    }

    /**
     * Create a name for the survey.
     *
     * @return The survey name.
     */
    protected String createSurveyName() {
        // Create survey filename.
        return "Survey-" + findNextSmallestSurveyNumber();
    }

    /**
     * Check each serialized survey file for the survey's number and
     * find the nest smallest number starting from one.
     *
     * @return The next smallest survey number.
     */
    protected int findNextSmallestSurveyNumber() {
        Integer num;
        boolean foundNumber;
        List<String> allSurveyNames = null;

        // The number to follow survey's name.
        Integer nextSmallest = 1;

        try {
            // Try to get the names of all surveys.
            allSurveyNames = getAllSurveyNames();
        } catch (IllegalStateException ignore) {
        }

        if (allSurveyNames != null && !allSurveyNames.isEmpty()) {
            // Find the next smallest survey number.
            do {
                // Assume we already found the next smallest number.
                foundNumber = true;

                for (String s : allSurveyNames) {
                    // Try to parse survey number from name.
                    num = parseSurveyNumber(s);

                    if (num != null) {
                        // If this number is already taken, increment and try again.
                        if (num.equals(nextSmallest)) {
                            foundNumber = false;
                            nextSmallest++;

                            break;
                        }
                    }
                }

                // If foundNumber is still true by the time it gets here,
                // then no other survey has taken the number.
            } while (!foundNumber);
        }

        return nextSmallest;
    }

    /**
     * Try to parse a survey's number from its name.
     *
     * @param surveyName the survey name
     * @return the survey number or null if it does not follow the
     * expected naming convention: Survey-[DIGIT]
     */
    protected Integer parseSurveyNumber(String surveyName) {
        String num;
        Integer result = null;

        try {
            // Try to get number from survey name.
            num = surveyName.split(NAME_SEPARATOR)[1];

            // Try to parse survey number from name.
            result = Integer.parseInt(num);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
        }

        return result;
    }

    /* *********************************************************************** */
    /*                  SERIALIZATION   &   DESERIALIZATION                    */
    /* *********************************************************************** */

    /**
     * Saves a Survey and its non-transient attributes using Serialization API.
     */
    public static void serialize(Survey survey) {
        // Serialize the survey to disk using the existing helper function
        SerializationHelper.serialize(Survey.class, survey, basePath, survey.getName());
    }

    /**
     * Deserializes a Survey that can be found at the given path
     *
     * @param path The path to the survey
     * @return The deserialized survey
     */
    public static Survey deserialize(String path) throws IOException, ClassNotFoundException {
        return SerializationHelper.deserialize(Survey.class, path);
    }

    /**
     * Retrieves the filenames of all available Surveys that are currently stored on disk in a List.
     *
     * @return List<String> for all available surveys filenames. This can be empty.
     */
    public static List<String> getAllSurveyNames() {
        return FileUtils.getAllFilenamesInDir(basePath);
    }

    /**
     * Retrieves the file paths of all available Surveys that are currently stored on disk in a List.
     *
     * @return List<String> for all available surveys file paths. This can be empty.
     */
    public static List<String> getAllSurveyFilePaths() {
        return FileUtils.getAllFilePathsInDir(basePath);
    }

    /**
     * Deletes the file denoted by this pathname.
     *
     * @param surveyPath The path to the survey to be deleted.
     * @return true if and only if the file is successfully deleted, otherwise false.
     */
    public static boolean delete(String surveyPath) throws IllegalArgumentException, SecurityException {
        return FileUtils.deleteFile(surveyPath);
    }
}
