package survey;

import survey.question.Question;
import utils.FileConfiguration;
import utils.FileUtils;
import utils.SerializationHelper;

import java.io.File;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Survey implements Serializable {
    private static long serialVersionUID = 1L;
    private static final String basePath = FileConfiguration.SERIALIZED_FILES_DIRECTORY + "Survey" + File.separator;
    private String surveyName;
    private QuestionFactory questionFactory;
    private List<Question> questionList;
    private List<Question> testquestionList;
    /*private SurveyResponse response;*/

    public Survey(QuestionFactory questionFactory) {
        this.questionFactory = questionFactory;
        questionList = new ArrayList<>();
        testquestionList = new ArrayList<>();
    }

    public String getSurveyName() {
        return surveyName;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void create() {
        String choiceStr;
        Question q;

        // Loop until user quits.
        do {
            // Display main menu.
            SurveyApp.out.displayMenu(QuestionFactory.QuestionMenu.PROMPT, QuestionFactory.QuestionMenu.OPTIONS);

            // Get user menu choice.
            choiceStr = SurveyApp.in.readValidMenuChoice(QuestionFactory.QuestionMenu.OPTIONS, -1);

            if (!SurveyApp.isNullOrEmpty(choiceStr)) {
                if (!choiceStr.equals(QuestionFactory.QuestionMenu.RETURN)) {
                    // Use survey.question factory to get new survey.question.
                    q = questionFactory.getQuestion(choiceStr);

                    try {
                        // Create survey.question specific attributes.
                        q.create();
                    } catch (NullPointerException ignore) {
                        continue;
                    }

                    // Add survey.question to survey.question list.
                    questionList.add(q);
                }
            } else {
                SurveyApp.displayInvalidInputMessage("choice");
            }
        } while (!choiceStr.equals(QuestionFactory.QuestionMenu.RETURN));
    }

    public void display() {
        for (Question q : questionList) {
            q.display();
        }
    }

    public static Survey load() {
        return null;
    }

    public void take() {
    }

    public void modify() {
    }

    /**
     * Saves a Survey and its non-transient attributes using Serialization API.
     *
     * @return The serialized survey file path.
     */
    public static String serialize(Survey survey) {
        String filename;
        String surveyName = survey.getSurveyName();

        // Create survey filename.
        if (SurveyApp.isNullOrEmpty(surveyName)) filename = SurveyApp.createFilename(survey);
        else filename = surveyName;

        // Serialize the survey to disk using the existing helper function
        return SerializationHelper.serialize(Survey.class, survey, basePath, filename);
    }

    /**
     * Deserializes a specific survey. The user will be presented with available Surveys to
     * deserialize.
     *
     * @return The deserialized Survey
     */
    public static Survey deserialize() throws IOException, ClassNotFoundException {
        // Use the existing utility function to allow the user to pick the survey from a
        // list of existing serialized surveys
        String selectedSurvey = FileUtils.listAndPickFileFromDir(basePath);

        // Use the existing deserialization function to handle it from here
        return deserialize(selectedSurvey);
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
     * Deserializes all available Surveys that are currently stored on disk in a List
     *
     * @return List<Survey> for all available surveys. This can be empty.
     */
    public static List<Survey> deserializeAllSurveys() throws IOException, ClassNotFoundException {
        List<Survey> allSurveys = new ArrayList<>();
        List<String> allPaths = FileUtils.getAllFilePathsInDir(basePath);
        for (String path : allPaths)
            allSurveys.add(deserialize(path));
        return allSurveys;
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
