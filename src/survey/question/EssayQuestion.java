package survey.question;

import survey.SurveyApp;

import java.util.ArrayList;
import java.util.List;

public class EssayQuestion extends Question {
    private String prompt;
    private int numResponses;

    public EssayQuestion() {
        super();
    }

    @Override
    public String getQuestionType() {
        return "Essay";
    }

    @Override
    public void create() {
        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of responses.
        numResponses = getValidNumResponses();
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayNote("Please give " + numResponses + " response(s).", true);
    }

    @Override
    public void modify() {
        // Modify the question prompt. If return value is true,
        // then user chose to return to the previous menu.
        boolean isReturn = modifyPrompt();

        // Test return value.
        if (!isReturn) modifyNumResponses();
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
                    SurveyApp.displayInvalidInputMessage(getResponseType());
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

    protected String getReasonWhyResponseIsInvalid(String response) {
        String reason = "";

        // Test for null or empty string.
        if (SurveyApp.isNullOrEmpty(response)) reason = "Your " + getResponseType() + " cannot be empty.";

        return reason;
    }
}
