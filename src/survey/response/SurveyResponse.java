package survey.response;

import survey.SurveyApp;
import utils.FileConfiguration;
import utils.SerializationHelper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SurveyResponse implements Serializable {
    protected static long serialVersionUID = 1L;
    protected static final String basePath = FileConfiguration.SERIALIZED_FILES_DIRECTORY + "Survey-Response" + File.separator;
    protected String surveyName;
    protected final List<QuestionResponse> responseList;


    public SurveyResponse(String surveyName) {
        this.surveyName = surveyName;
        responseList = new ArrayList<>();
    }

    public String getName() {
        return surveyName + "-Response";
    }

    public boolean add(QuestionResponse qr) {
        return responseList.add(qr);
    }

    public QuestionResponse get(int index) {
        return responseList.get(index);
    }

    public void save() {
        try {
            // Try to serialize survey to file on disk.
            SurveyResponse.serialize(this);
            SurveyApp.out.displayNote("Response saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            SurveyApp.out.displayNote("There was an error while saving your survey response.");
        }
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
