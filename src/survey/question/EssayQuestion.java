package survey.question;

import survey.SurveyApp;
import utils.Validation;

public class EssayQuestion extends Question {

    public EssayQuestion() {
        super();
        questionType = "Essay";
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
        SurveyApp.out.displayQuestion(new String[]{
                prompt,
                "Please give " + numResponses + " " + responseType + "(s)."
        });
    }

    @Override
    public void modify() {
        // Modify the question prompt. If return value is true,
        // then user chose to return to the previous menu.
        boolean isReturn = modifyPrompt();

        // Test return value.
        if (!isReturn) modifyNumResponses();
    }

    /**
     * Determines if the essay response is valid. If it is invalid, report why.
     *
     * @return the response string
     */
    protected String getValidResponse() {
        // Record user response.
        String response = SurveyApp.in.readQuestionResponse();

        // Test for null or blank string.
        if (!Validation.isNullOrBlank(response))
            SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

        return response;
    }
}
