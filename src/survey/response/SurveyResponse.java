package survey.response;

import menu.SurveyMenu;
import survey.Survey;
import survey.SurveyApp;
import utils.FileConfiguration;
import utils.FileUtils;
import utils.SerializationHelper;
import utils.Validation;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SurveyResponse implements Serializable {
    protected static long serialVersionUID = 1L;
    protected static final String basePath = FileConfiguration.SERIALIZED_FILES_DIRECTORY + "Survey-Response" + File.separator;
    protected String name;
    protected String surveyName;
    protected final List<QuestionResponse> responseList;


    public SurveyResponse(String surveyName) {
        this.surveyName = surveyName;
        responseList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean add(QuestionResponse qr) {
        return responseList.add(qr);
    }

    public QuestionResponse get(int index) {
        return responseList.get(index);
    }

    public int size() {
        return responseList.size();
    }

    /**
     * Creates a new name for the survey response and save it.
     */
    public void save() {
        // Create survey response name.
        name = createSurveyResponseName();

        try {
            // Try to serialize survey to file on disk.
            SurveyResponse.serialize(this);
            SurveyApp.out.displayNote("Response saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            SurveyApp.out.displayNote("There was an error while saving your survey response.");
        }
    }

    /**
     * Create a name for the survey response.
     *
     * @return the survey response name
     */
    protected String createSurveyResponseName() {
        // Create survey response filename.
        return surveyName + "-Response-" + findNextSmallestSurveyResponseNumber();
    }

    /**
     * Check each serialized survey response file for the specified survey in
     * surveyName. Out of the responses for the specified survey, find the next
     * smallest number for this survey response starting from one.
     *
     * @return the next smallest survey response number.
     */
    protected int findNextSmallestSurveyResponseNumber() {
        Integer num;
        boolean foundNumber;

        // The number to follow survey response's name.
        Integer nextSmallest = 1;

        // Get the names of all survey responses for this survey.
        List<String> filteredSurveyResponseNames = getFilteredSurveyResponseNames();

        if (filteredSurveyResponseNames != null && !filteredSurveyResponseNames.isEmpty()) {
            // Find the next smallest survey response number.
            do {
                // Assume we already found the next smallest number.
                foundNumber = true;

                for (String s : filteredSurveyResponseNames) {
                    // Parse survey response number from name.
                    num = parseSurveyResponseNumber(s);

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
                // then no other survey response has taken the number.
            } while (!foundNumber);
        }

        return nextSmallest;
    }

    /**
     * Get the list of all survey response names and filter out the survey responses
     * from all other surveys.
     *
     * @return the filtered list of survey response names
     */
    protected List<String> getFilteredSurveyResponseNames() {
        int surveyNameLength;
        String nameSubstring;
        List<String> allSurveyResponseNames = null;
        List<String> result = new ArrayList<>();

        try {
            // Try to get the names of all survey responses.
            allSurveyResponseNames = getAllSurveyResponseNames();
        } catch (IllegalStateException ignore) {
        }

        if (allSurveyResponseNames != null && !allSurveyResponseNames.isEmpty()) {
            // Get the length of the survey response survey's name.
            surveyNameLength = surveyName.length();

            // Filter out survey responses for all other surveys.
            for (String s : allSurveyResponseNames) {
                // Get the survey name from list of all survey response name.
                nameSubstring = s.substring(0, surveyNameLength);

                // Test if this survey name from list of all survey response names is
                // equal to the survey responses survey's name. If it is, then add it.
                if (nameSubstring.equals(surveyName)) result.add(s);
            }
        }

        return result;
    }

    /**
     * Prompt the user to choose one of the available survey responses and get its path.
     *
     * @param surveyFilename the survey filename.
     * @param action         the action to be performed on the survey response
     * @return a path to the chosen survey response or null if no survey responses are available.
     */
    public static String getSurveyResponsePath(String surveyFilename, String action) {
        String choice;
        List<String> filteredNames;
        String result = null;
        List<String> filteredPaths = null;

        try {
            // Try to get filtered survey response file paths.
            filteredPaths = getFilteredSurveyResponseFilePaths(surveyFilename);
        } catch (IllegalStateException ignore) {
            SurveyApp.out.displayNote(surveyFilename + " has not been taken yet.");
        }

        // Test survey response path list is not null or empty.
        if (!Validation.isNullOrEmpty(filteredPaths)) {
            // Get filtered survey response filenames for this survey.
            filteredNames = FileUtils.parseAllFilenames(filteredPaths);

            // Add the return option.
            filteredNames.add(SurveyMenu.RETURN);

            // Get user chosen survey response.
            choice = SurveyApp.getUserMenuChoice("Please select a response to " + action + ":", filteredNames);

            // Test user did not choose to return.
            if (!choice.equals(SurveyMenu.RETURN)) {
                // Get survey response path from list.
                result = filteredPaths.get(filteredNames.indexOf(choice));
            }
        }

        return result;
    }

    /**
     * Get a list of all survey response file paths and filter out the survey responses
     * from all other surveys.
     *
     * @param surveyName the survey name
     * @return a filtered list of survey response file paths
     */
    protected static List<String> getSurveyResponsePath(String surveyName) {
        int surveyNameLength;
        String nameSubstring;
        List<String> allSurveyResponsePaths = null;
        List<String> result = new ArrayList<>();

        try {
            // Try to get the paths of all survey responses.
            allSurveyResponsePaths = getAllSurveyResponsePaths();
        } catch (IllegalStateException ignore) {
        }

        if (allSurveyResponsePaths != null && !allSurveyResponsePaths.isEmpty()) {
            // Get the length of the survey response survey's name.
            surveyNameLength = surveyName.length();

            // Filter out survey responses for all other surveys.
            for (String s : allSurveyResponsePaths) {
                // Get the survey name from list of all survey response name.
                nameSubstring = s.substring(0, surveyNameLength);

                // Test if this survey name from list of all survey response names is
                // equal to the survey responses survey's name. If it is, then add it.
                if (nameSubstring.equals(surveyName)) result.add(s);
            }
        }

        return result;
    }

    /**
     * Retrieves the filenames of all available survey responses that are
     * currently stored on disk in a List.
     *
     * @return List<String> for all available survey response filenames.
     * This can be empty.
     */
    protected static List<String> getAllSurveyResponseNames() {
        return FileUtils.getAllFilenamesInDir(basePath);
    }

    /**
     * Retrieves the file paths of all available survey responses that are
     * currently stored on disk in a List.
     *
     * @return List<String> for all available survey response file paths.
     * This can be empty.
     */
    protected static List<String> getAllSurveyResponsePaths() {
        return FileUtils.getAllFilePathsInDir(basePath);
    }

    /**
     * Try to parse a survey response's number from its name.
     *
     * @param surveyResponseName the survey response name
     * @return the survey response number or null if it does not follow the
     * expected naming convention: Survey-[DIGIT]-Response-[DIGIT]
     */
    protected Integer parseSurveyResponseNumber(String surveyResponseName) {
        String num;
        Integer result = null;

        try {
            // Try to get number from survey name.
            num = surveyResponseName.split(Survey.NAME_SEPARATOR)[3];

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
     * Saves a SurveyResponse and its non-transient attributes using Serialization API.
     *
     * @return The serialized survey response file path.
     */
    public static String serialize(SurveyResponse surveyResponse) {
        // Serialize the survey response to disk using the existing helper function
        return SerializationHelper.serialize(SurveyResponse.class, surveyResponse, basePath, surveyResponse.getName());
    }

    /**
     * Deserializes a SurveyResponse that can be found at the given path
     *
     * @param path The path to the survey response
     * @return The deserialized survey response
     */
    public static SurveyResponse deserialize(String path) throws IOException, ClassNotFoundException {
        return SerializationHelper.deserialize(SurveyResponse.class, path);
    }

    public static List<SurveyResponse> deserializeResponsesOfSurvey(String surveyName) {
        int i;
        boolean isCorrupted;
        String surveyResponseName;
        int corruptedCount = 0;
        List<String> allSurveyResponseNames;
        List<SurveyResponse> result = null;
        SurveyResponse sr = null;

        // Get length of survey name.
        int length = surveyName.length();

        // Get all survey response file paths.
        List<String> allSurveyResponseFilePaths = getAllSurveyResponseFilePaths();

        // Test survey response path list is not null or empty.
        if (!Validation.isNullOrEmpty(allSurveyResponseFilePaths)) {
            // Get all survey response filenames.
            allSurveyResponseNames = FileUtils.parseAllFilenames(allSurveyResponseFilePaths);

            // Initialize survey response list.
            result = new ArrayList<>();

            for (i = 0; i < allSurveyResponseNames.size(); i++) {
                // Get survey response name.
                surveyResponseName = allSurveyResponseNames.get(i);

                // Test if survey response name contains survey name.
                if (surveyResponseName.substring(0, length).equals(surveyName)) {
                    try {
                        // Deserialize survey response.
                        sr = deserialize(allSurveyResponseFilePaths.get(i));
                        isCorrupted = false;
                    } catch (IOException | ClassNotFoundException ignore) {
                        // Survey file is likely out of sync with survey class.
                        isCorrupted = true;
                        corruptedCount++;
                    }

                    // Test if file is not corrupted.
                    if (!isCorrupted) {
                        // Add deserialized survey response to result.
                        result.add(sr);
                    }
                }
            }

            // Test if any survey response files are corrupted.
            if (corruptedCount > 0)
                SurveyApp.out.displayAllNotes(new String[]{
                        corruptedCount + " survey response file(s) have become corrupted",
                        "This is likely because SurveyApp has been updated since this file was saved.",
                        "These survey responses were not includes."
                });
        }

        return result;
    }

    /**
     * Retrieves the filenames of all available SurveyResponses that are currently stored on disk in a List.
     *
     * @return List<String> for all available survey responses filenames. This can be empty.
     */
    public static List<String> getAllSurveyResponseFileNames() {
        return FileUtils.getAllFilenamesInDir(basePath);
    }

    /**
     * Retrieves the file paths of all available SurveyResponses that are currently stored on disk in a List.
     *
     * @return List<String> for all available survey responses file paths. This can be empty.
     */
    public static List<String> getAllSurveyResponseFilePaths() {
        return FileUtils.getAllFilePathsInDir(basePath);
    }

    public static List<String> getFilteredSurveyResponseFilePaths(String surveyFilename) {
        int length, i;
        List<String> allFilenames;
        List<String> result = null;

        // Get all response paths.
        List<String> allPaths = FileUtils.getAllFilePathsInDir(basePath);

        // Test response filenames list is not empty.
        if (!allPaths.isEmpty()) {
            // Get all response filenames.
            allFilenames = FileUtils.parseAllFilenames(allPaths);

            // Initialize filtered response file path list.
            result = new ArrayList<>();

            // Get length of survey name.
            length = surveyFilename.length();

            // Filter response file path list.
            for (i = 0; i < allFilenames.size(); i++)
                if (allFilenames.get(i).substring(0, length).equals(surveyFilename))
                    result.add(allPaths.get(i));
        }

        return result;
    }
}
