package survey.response;

import survey.Survey;
import survey.SurveyApp;
import utils.FileConfiguration;
import utils.FileUtils;
import utils.SerializationHelper;

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
     * @return the filtered list of survey response names.
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
     * Retrieves the filenames of all available survey responses that are
     * currently stored on disk in a List.
     *
     * @return List<String> for all available survey response filenames.
     * This can be empty.
     */
    protected List<String> getAllSurveyResponseNames() {
        return FileUtils.getAllFilenamesInDir(basePath);
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
}
