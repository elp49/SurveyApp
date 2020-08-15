package survey.question;

import survey.SurveyApp;

import java.util.ArrayList;
import java.util.List;

public class ShortAnswerQuestion extends EssayQuestion {
    private int numResponses;
    private final int responseCharLimit = 64;

    public ShortAnswerQuestion() {
        super();
    }

    @Override
    public String getQuestionType() {
        return "Short Answer";
    }

    @Override
    public String getResponseType() {
        return "answer(s)";
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
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayNote(new String[]{
                "Limit your answer(s) to " + responseCharLimit + " characters.",
                "Please give " + numResponses + " answer(s)."
        }, true);
    }

    @Override
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
    }

    @Override
    protected String getReasonWhyResponseIsInvalid(String response) {
        String reason = "";

        // Test for null or empty string.
        if (SurveyApp.isNullOrEmpty(response))
            reason = "Your " + getResponseType() + " cannot be empty.";

            // Test for response length greater than limit.
        else if (response.length() > responseCharLimit)
            reason = "Your " + getResponseType() + " of " + response.length() + " characters exceeds the limit of " + responseCharLimit + ".";

        return reason;
    }
}
