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

    @Override
    protected String readPossibleQuestionResponse() {
        boolean isPossibleResponse;
        String response;

        do {
            // Get question response.
            response = SurveyApp.in.readQuestionResponse();

            // Test for null or blank string.
            if (!(isPossibleResponse = !Validation.isNullOrBlank(response)))
                SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

                // Test for response length within character limit.
            else if (!(isPossibleResponse = response.length() <= responseCharLimit))
                SurveyApp.out.displayNote("Your " + responseType + " of " + response.length()
                        + " characters exceeds the limit of " + responseCharLimit + ".");

            // If the user enters an impossible question response,
            // then isPossibleResponse will be false.
        } while (!isPossibleResponse);

        return response;
    }
}
