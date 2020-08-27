package survey;

import menu.CreateQuestionMenu;
import menu.DeleteMenu;
import menu.ModifyQuestionMenu;
import menu.SurveyMenu;
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
    private static long serialVersionUID = 1L;
    private static final String basePath = FileConfiguration.SERIALIZED_FILES_DIRECTORY + "Survey" + File.separator;
    protected String name;
    protected String surveyType;
    public final static String NAME_SEPARATOR = "-";
    protected List<Question> questionList = new ArrayList<>();
    protected transient SurveyResponse surveyResponse = null;

    public Survey() {
        surveyType = "Survey";
        name = createSurveyName();
    }

    public String getName() {
        return name;
    }

    protected int getNumQuestions() {
        return questionList.size();
    }

    protected Question getQuestion(int index) {
        return questionList.get(index);
    }

    protected List<Question> getQuestionList() {
        return questionList;
    }

    public void create() {
        String choice;
        Question q;
        QuestionFactory qf = new QuestionFactory();

        // Loop until user quits.
        do {
            // Get user choice from question menu.
            choice = SurveyApp.getUserMenuChoice(CreateQuestionMenu.PROMPT, CreateQuestionMenu.OPTIONS);

            if (!choice.equals(CreateQuestionMenu.RETURN)) {
                // Use survey.question factory to get new question.
                q = qf.getQuestion(choice);

                try {
                    // Create survey.question specific attributes.
                    q.create();
                } catch (NullPointerException ignore) {
                    SurveyApp.out.displayNote("That type of question cannot be created right now. Please try another.");
                    q = null;
                }

                if (q != null) {
                    // Add survey.question to question list.
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
        // Deserialize user chosen survey.
        Survey result = deserializeChosenSurvey("load");

        // Test survey is not null.
        if (result != null)
            SurveyApp.out.displayNote("Loaded successfully.");

        return result;
    }

    public void save() {
        try {
            // Try to serialize survey to file on disk.
            serialize(this);
            SurveyApp.out.displayNote("Saved survey successfully.");
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
                options.add(ModifyQuestionMenu.RETURN);

                // Get user chosen question.
                choice = SurveyApp.getUserMenuChoice(new String[]{
                        "Which question do you wish to modify?",
                        "Below is the list of questions you have created."
                }, options);

                if (!(isReturn = choice.equals(ModifyQuestionMenu.RETURN))) {
                    // Get index of question to be modified.
                    index = options.indexOf(choice);

                    // Modify question.
                    questionList.get(index).modify(surveyType.equalsIgnoreCase("test"));
                }
            } while (!isReturn);
        } else {
            SurveyApp.out.displayNote("Your survey does not have any questions to modify yet.");
        }
    }

    public static void tabulateSurvey() {
        // Deserialize user chosen survey.
        Survey survey = deserializeChosenSurvey("tabulate");

        // Test survey is not null.
        if (survey != null)
            tabulate(survey);

        else
            SurveyApp.out.displayNote("You must create a survey first before you can tabulate it.");
    }

    public static void tabulate(Survey survey) {
        int i;
        Question q;

        // Deserialize all responses to this survey.
        List<SurveyResponse> surveyResponseList = SurveyResponse.deserializeResponsesOfSurvey(survey.getName());

        // Initialize question response list.
        List<QuestionResponse> questionResponseList = new ArrayList<>();

        for (i = 0; i < survey.getNumQuestions(); i++) {
            // Get survey question.
            q = survey.getQuestion(i);

            // Add each response for question to question response list.
            for (SurveyResponse sr : surveyResponseList)
                questionResponseList.add(sr.get(i));

            q.tabulate(questionResponseList);

            // Clean up question response list.
            questionResponseList.clear();
        }
    }

    /**
     * Prompt the user to choose one of the available surveys and get its path.
     *
     * @param action the action to be performed on the survey
     * @return The path to the chosen survey or null if no surveys are available.
     */
    protected static String getSurveyPath(String action) {
        String choice;
        List<String> allSurveyNames;
        String result = null;
        List<String> allSurveyPaths = null;

        try {
            // Try to get all survey file paths.
            allSurveyPaths = getAllSurveyFilePaths();
        } catch (IllegalStateException ignore) {
            SurveyApp.out.displayNote("You have not saved any surveys yet.");
        }

        // Test survey path list is not null or empty.
        if (!Validation.isNullOrEmpty(allSurveyPaths)) {
            // Get all survey filenames.
            allSurveyNames = FileUtils.parseAllFilenames(allSurveyPaths);

            // Add the option to return to previous menu.
            allSurveyNames.add(SurveyMenu.RETURN);

            // Get user chosen survey.
            choice = SurveyApp.getUserMenuChoice("Please select a file to " + action + ":", allSurveyNames);

            if (!choice.equals(SurveyMenu.RETURN)) {
                // Get survey path from list.
                result = allSurveyPaths.get(allSurveyNames.indexOf(choice));
            }
        }

        return result;
    }

    /**
     * Create a name for the survey.
     *
     * @return a survey name.
     */
    protected String createSurveyName() {
        // Create survey filename.
        return surveyType + NAME_SEPARATOR + findNextSmallestSurveyNumber();
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
     * Try to parse a survey's number from its file name.
     *
     * @param surveyFileName the survey file name
     * @return the survey number or null if it does not follow the
     * expected naming convention: Survey-[DIGIT]
     */
    protected Integer parseSurveyNumber(String surveyFileName) {
        String num;
        Integer result = null;

        try {
            // Try to get number from survey name.
            num = surveyFileName.split(NAME_SEPARATOR)[1];

            // Try to parse survey number from name.
            result = Integer.parseInt(num);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
        }

        return result;
    }

    /**
     * Try to parse a survey's name.
     *
     * @param surveyFileName the survey file name
     * @return the survey name or null if it does not follow the
     * expected naming convention: Survey-[DIGIT]
     */
    protected String parseSurveyName(String surveyFileName) {
        String result = null;

        try {
            // Try to get number from survey name.
            result = surveyFileName.split(NAME_SEPARATOR)[0];
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

    public static Survey deserializeChosenSurvey(String action) {
        String surveyPath;
        boolean isNullOrBlank;
        boolean isCorrupted = false;
        Survey result = null;

        do {
            surveyPath = getSurveyPath(action);
            if (!(isNullOrBlank = Validation.isNullOrBlank(surveyPath))) {
                try {
                    // Deserialize the chosen survey.
                    result = deserialize(surveyPath);
                    isCorrupted = false;
                } catch (IOException | ClassNotFoundException ignore) {
                    // Survey file is likely out of sync with survey class.
                    SurveyApp.out.displayAllNotes(new String[]{
                            "This serialized survey file has become corrupted.",
                            "This is likely because SurveyApp has been updated since this file was saved."
                    });

                    isCorrupted = true;

                    handleCorruptedSurvey(surveyPath);
                }
            }

            // If the user has chosen a non-corrupted survey to load
            // or if they have no surveys to load, then return.
            // If a survey was found to be corrupted, then continue.
        } while (isCorrupted && !isNullOrBlank);

        return result;
    }

    public static void handleCorruptedSurvey(String surveyPath) {
        // Get user choice from delete menu.
        String choice = SurveyApp.getUserMenuChoice(DeleteMenu.PROMPT, DeleteMenu.OPTIONS);

        if (choice.equals(DeleteMenu.DELETE)) {
            try {
                // Try to delete survey.
                if (delete(surveyPath)) SurveyApp.out.displayNote("Successfully deleted.");
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
