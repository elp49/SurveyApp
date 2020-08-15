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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Survey implements Serializable {
    protected static long serialVersionUID = 1L;
    protected static final String basePath = FileConfiguration.SERIALIZED_FILES_DIRECTORY + "Survey" + File.separator;
    protected String name;
    protected final QuestionFactory questionFactory;
    protected final List<Question> questionList;

    public Survey(QuestionFactory questionFactory) {
        this.questionFactory = questionFactory;
        questionList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void create() {
        String choice;
        Question q;

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
                    continue;
                }

                // Add survey.question to survey.question list.
                questionList.add(q);
            }
        } while (!choice.equals(CreateQuestionMenu.RETURN));
    }

    public void display() {
        for (Question q : questionList) {
            q.display();
        }
    }

    /**
     * Get the path of a survey to load and try to deserialize it.
     *
     * @return The deserialized survey or null if no surveys can be loaded.
     */
    public static Survey load() {
        String surveyPath;
        boolean isNullOrEmpty;
        boolean isCorrupted = false;
        Survey result = null;

        do {
            surveyPath = getSurveyPath();
            if (!(isNullOrEmpty = SurveyApp.isNullOrEmpty(surveyPath))) {
                try {
                    result = Survey.deserialize(surveyPath);

                    SurveyApp.out.displayNote("Loaded successfully.");

                    isCorrupted = false;
                } catch (IOException | ClassNotFoundException ignore) {
                    // Survey file is likely out of sync with survey class.
                    SurveyApp.out.displayNote(new String[]{
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
        } while (isCorrupted && !isNullOrEmpty);

        return result;
    }

    public void save() {
        // Create survey name.
        if (SurveyApp.isNullOrEmpty(name)) name = createSurveyName();

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
        String questionType;
        List<String> responseList;

        // Create survey response object.
        SurveyResponse surveyResponse = new SurveyResponse(name);

        // Create survey name.
        if (SurveyApp.isNullOrEmpty(name)) name = createSurveyName();

        // Display survey name.
        SurveyApp.out.displaySurveyName(name);

        // Loop through each question.
        for (Question q : questionList) {
            // Get question type;
            questionType = q.getQuestionType();

            // Display question.
            q.display();

            // Record valid question response.
            responseList = q.getValidResponseList();

            // Add question response to survey response list.
            surveyResponse.add(new QuestionResponse(questionType, responseList));
        }

        // Save response.
        surveyResponse.save();
    }

    public void modify() {
        String choice;
        boolean isReturn;
        int index;
        List<String> options = new ArrayList<>();

        if (!questionList.isEmpty()) {
            do {
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
                    index = options.indexOf(choice);
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
    private static String getSurveyPath() {
        String choice;
        String result = null;
        boolean isNullOrEmpty = false;
        List<String> allSurveyPaths = null;

        try {
            // Try to get all survey file paths.
            allSurveyPaths = Survey.getAllSurveyFilePaths();
        } catch (IllegalStateException ignore) {
            SurveyApp.out.displayNote("You have not saved any surveys yet.");
        }

        if (allSurveyPaths != null && !allSurveyPaths.isEmpty()) {
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
                SurveyApp.out.displayNote(new String[]{
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
        Integer surveyNumber = 1;

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
                        if (num.equals(surveyNumber)) {
                            foundNumber = false;
                            surveyNumber++;
                            break;
                        }
                    }
                }

                // If foundNumber is still true by the time it gets here,
                // then no other survey has taken the number it.
            } while (!foundNumber);
        }

        return surveyNumber;
    }

    /**
     * Try to parse a survey's number from its name.
     *
     * @return The survey number or null if it does not follow the
     * expected naming convention.
     */
    protected Integer parseSurveyNumber(String surveyName) {
        Integer result = null;

        // Get indices of survey number.
        int beginIndex = surveyName.lastIndexOf("-") + 1;
        int endIndex = surveyName.length();

        // Try to parse survey number from name.
        try {
            result = Integer.parseInt(surveyName.substring(beginIndex, endIndex));
        } catch (NumberFormatException ignore) {
        }

        return result;
    }

    /* *********************************************************************** */
    /*                  SERIALIZATION   &   DESERIALIZATION                    */
    /* *********************************************************************** */

    /**
     * Saves a Survey and its non-transient attributes using Serialization API.
     *
     * @return The serialized survey file path.
     */
    public static String serialize(Survey survey) {
        // Serialize the survey to disk using the existing helper function
        return SerializationHelper.serialize(Survey.class, survey, basePath, survey.getName());
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
