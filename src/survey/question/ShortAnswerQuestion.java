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
        SurveyApp.out.displayQuestionPrompt(new String[]{
                prompt,
                "Limit your " + responseType + "(s) to " + responseCharLimit + " characters.",
                "Please give " + numResponses + " " + responseType + "(s) ."
        });
    }

    /*@Override
    public List<String> getValidResponseList() {
        int i;
        String response, reason;
        boolean isNullOrEmpty;
        List<String> responseList = new ArrayList<>();

        // Loop until user gives valid response(s).
        for (i = 1; i <= numResponses; i++) {
            do {
                // Record response.
                response = SurveyApp.in.readQuestionResponse();

                // Get reason why response is invalid or empty string if is valid.
                reason = getReasonWhyResponseIsInvalid(response);

                // Test if valid response.
                if (!(isNullOrEmpty = SurveyApp.isNullOrEmpty(reason))) {
                    SurveyApp.displayInvalidInputMessage("answer");
                    SurveyApp.out.displayNote(reason);
                }

                // If the user response is invalid, getReasonWhyResponseIsInvalid will
                // return the reason why as a string and isNullOrEmpty will be false.
            } while (!isNullOrEmpty);

            // Add response to response list.
            responseList.add(response);
        }

        return responseList;
    }*/


    /**
     * Determines if the question response is a valid short answer.
     *
     * @param response the short answer.
     * @return true if the response is valid, otherwise false.
     */
    @Override
    protected boolean performResponseValidation(String response) {
        boolean isValid;

        // Test for null or blank string.
        if (!(isValid = !Validation.isNullOrBlank(response)))
            SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

            // Test for response length below character limit.
        else if (!(isValid = response.length() <= responseCharLimit))
            SurveyApp.out.displayNote("Your " + responseType + " of " + response.length()
                    + " characters exceeds the limit of " + responseCharLimit + ".");

        return isValid;
    }
}
