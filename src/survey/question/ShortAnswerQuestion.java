package survey.question;

import survey.SurveyApp;
import utils.Validation;

public class ShortAnswerQuestion extends EssayQuestion {
    private final int responseCharLimit = 64;

    public ShortAnswerQuestion() {
        super();
        questionType = "Short Answer";
        responseType = "answer";
    }

    @Override
    public void create() {
        SurveyApp.out.displayNote("Answers will be limited to " + responseCharLimit + " characters.");

        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of responses.
        numResponses = getValidNumResponses();
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestion(new String[]{
                prompt,
                "Limit your " + responseType + "(s) to " + responseCharLimit + " characters.",
                "Please give " + numResponses + " " + responseType + "(s)."
        });
    }

    /**
     * Determines if the short answer is valid. If it is invalid, report why.
     *
     * @return the response string
     */
    protected String getValidResponse() {
        // Record user response.
        String response = SurveyApp.in.readQuestionResponse();

        // Test for null or blank string.
        if (!Validation.isNullOrBlank(response))
            SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

            // Test for response length within character limit.
        else if (!(response.length() <= responseCharLimit)) {
            SurveyApp.out.displayNote("Your " + responseType + " of " + response.length()
                    + " characters exceeds the limit of " + responseCharLimit + ".");

            // Clean up invalid response.
            response = null;
        }

        return response;
    }
}
