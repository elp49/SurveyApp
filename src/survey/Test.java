package survey;

import menu.CreateQuestionMenu;
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
import java.util.List;

public class Test extends Survey {
    private static long serialVersionUID = 1L;
    private static final String basePath = FileConfiguration.SERIALIZED_FILES_DIRECTORY + "Test" + File.separator;
    protected SurveyResponse answerKey;

    public Test() {
        surveyType = "Test";
        name = createSurveyName();
        answerKey = new SurveyResponse(name);
    }

    /**
     * Create a name for the survey.
     *
     * @return a survey name.
     */
    @Override
    protected String createSurveyName() {
        // Create survey filename.
        return surveyType + NAME_SEPARATOR + findNextSmallestTestNumber();
    }

    @Override
    public void create() {
        String choice;
        Question q;
        QuestionResponse correctAnswer;
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
                    // Read correct answer.
                    correctAnswer = q.readCorrectAnswer();

                    // Add correct answer to answer key.
                    answerKey.add(correctAnswer);

                    // Add survey.question to question list.
                    questionList.add(q);

                    SurveyApp.out.displayNote("Successfully added new " + q.getQuestionType() + " question.");
                }
            }
        } while (!choice.equals(CreateQuestionMenu.RETURN));
    }

    public void displayWithAnswers() {
        int i;
        Question q;

        // Display survey name.
        displayName();

        // Display each question in survey.
        for (i = 0; i < questionList.size(); i++) {
            // Get question.
            q = questionList.get(i);

            // Display question
            q.display();

            // Display correct answer.
            q.displayAnswer(answerKey.get(i));
        }
    }

    /**
     * Get the path of a test to load and try to deserialize it.
     *
     * @return The deserialized test or null if no tests can be loaded.
     */
    public static Test load() {
        // Deserialize user chosen test.
        Test result = deserializeChosenTest("load");

        // Test not null.
        if (result != null)
            SurveyApp.out.displayNote("Loaded successfully.");

        return result;
    }

    @Override
    public void save() {
        try {
            // Try to serialize test to file on disk.
            serialize(this);
            SurveyApp.out.displayNote("Saved test successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            SurveyApp.out.displayNote("There was an error while saving your test.");
        }
    }

    public static void tabulateTest() {
        // Deserialize user chosen test.
        Test test = deserializeChosenTest("tabulate");

        // Test for not null.
        if (test != null)
            tabulate(test);

        else
            SurveyApp.out.displayNote("You must create a test first before you can tabulate it.");
    }


    public static void grade() {
        int numQuestions, pointsPerQuestion, i;
        String responsePath;
        List<Question> ql;
        Question q;
        int grade = 0;
        int numNonGradable = 0;
        int autoGradedPoints = 0;
        int POSSIBLE_POINTS = 100;
        SurveyResponse sr = null;

        // Deserialize user chosen test.
        Test test = deserializeChosenTest("grade");

        // Test for null.
        if (test != null) {

            // Get test response path.
            responsePath = SurveyResponse.getSurveyResponsePath(test.getName(), "grade");

            // Test path is not null or blank.
            if (!Validation.isNullOrBlank(responsePath)) {
                // Try to deserialize test response.
                try {
                    sr = SurveyResponse.deserialize(responsePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    SurveyApp.out.displayNote("There was an error while grading this test.");
                }

                if (sr != null) {
                    // Get test question list.
                    ql = test.getQuestionList();

                    // Get number of questions.
                    numQuestions = ql.size();

                    // Get number of points per question.
                    pointsPerQuestion = POSSIBLE_POINTS / numQuestions;

                    // Grade each question.
                    for (i = 0; i < numQuestions; i++) {
                        q = ql.get(i);

                        // Test not essay question.
                        if (!q.getQuestionType().equalsIgnoreCase("essay")) {
                            // Test if response is correct.
                            if (q.isCorrectResponse(test.answerKey.get(i), sr.get(i)))
                                grade += pointsPerQuestion;

                            // Increment auto graded
                            autoGradedPoints += pointsPerQuestion;
                        } else {
                            numNonGradable++;
                        }
                    }

                    // Display grade.
                    SurveyApp.out.displayNote("Your received a score of " + grade + " on the test. The test was worth 100 points");

                    if (numNonGradable > 0)
                        SurveyApp.out.displayNote("Only " + autoGradedPoints + " of those points could be auto graded because there was " + numNonGradable + " essay questions");
                    else
                        SurveyApp.out.displayNote("All questions were auto graded.");
                }
            } else {
                SurveyApp.out.displayNote("This test has not been taken yet.");
            }
        } else {
            SurveyApp.out.displayNote("You must create a test first before you can grade it.");
        }
    }

    /**
     * Prompt the user to choose one of the available tests and get its path.
     *
     * @param action the action to be performed on the test
     * @return The path to the chosen test or null if no tests are available.
     */
    protected static String getTestPath(String action) {
        String choice;
        List<String> allTestNames;
        String result = null;
        List<String> allTestPaths = null;

        try {
            // Try to get all test file paths.
            allTestPaths = getAllTestFilePaths();
        } catch (IllegalStateException ignore) {
            SurveyApp.out.displayNote("You have not saved any tests yet.");
        }

        // Test path list is not null or empty.
        if (!Validation.isNullOrEmpty(allTestPaths)) {
            // Get all test filenames.
            allTestNames = FileUtils.parseAllFilenames(allTestPaths);

            // Add the return option.
            allTestNames.add(SurveyMenu.RETURN);

            // Get user chosen test.
            choice = SurveyApp.getUserMenuChoice("Please select a test to " + action + ":", allTestNames);

            if (!choice.equals(SurveyMenu.RETURN)) {
                // Get survey path from list.
                result = allTestPaths.get(allTestNames.indexOf(choice));
            }
        }

        return result;
    }

    /**
     * Check each serialized test file for the test's number and
     * find the nest smallest number starting from one.
     *
     * @return the next smallest test number.
     */
    protected int findNextSmallestTestNumber() {
        Integer num;
        boolean foundNumber;
        List<String> allSurveyNames = null;

        // The number to follow survey's name.
        Integer nextSmallest = 1;

        try {
            // Try to get the names of all surveys.
            allSurveyNames = getAllTestNames();
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

    /* *********************************************************************** */
    /*                  SERIALIZATION   &   DESERIALIZATION                    */
    /* *********************************************************************** */

    /**
     * Saves a test and its non-transient attributes using Serialization API.
     */
    public static void serialize(Test test) {
        // Serialize the survey to disk using the existing helper function
        SerializationHelper.serialize(Test.class, test, basePath, test.getName());
    }

    /**
     * Deserializes a test that can be found at the given path
     *
     * @param path The path to the test
     * @return The deserialized test
     */
    public static Test deserialize(String path) throws IOException, ClassNotFoundException {
        return SerializationHelper.deserialize(Test.class, path);
    }

    public static Test deserializeChosenTest(String action) {
        String testPath;
        boolean isNullOrBlank;
        boolean isCorrupted = false;
        Test result = null;

        do {
            testPath = getTestPath(action);
            if (!(isNullOrBlank = Validation.isNullOrBlank(testPath))) {
                try {
                    // Deserialize the chosen test.
                    result = deserialize(testPath);
                    isCorrupted = false;
                } catch (IOException | ClassNotFoundException ignore) {
                    // Test file is likely out of sync with survey class.
                    SurveyApp.out.displayAllNotes(new String[]{
                            "This serialized test file has become corrupted.",
                            "This is likely because SurveyApp has been updated since this file was saved."
                    });

                    isCorrupted = true;

                    handleCorruptedSurvey(testPath);
                }
            }

            // If the user has chosen a non-corrupted test to load
            // or if they have no tests to load, then quit.
            // If a test was found to be corrupted, then continue.
        } while (isCorrupted && !isNullOrBlank);

        return result;
    }

    /**
     * Retrieves the file paths of all available tests that are currently stored on disk in a List.
     *
     * @return List<String> for all available tests file paths. This can be empty.
     */
    public static List<String> getAllTestFilePaths() {
        return FileUtils.getAllFilePathsInDir(basePath);
    }

    public static List<String> getAllTestNames() {
        return FileUtils.getAllFilenamesInDir(basePath);
    }
}
