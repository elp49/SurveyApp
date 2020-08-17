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
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayNote("Please give " + numResponses + " " + responseType + "s.", true);
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
     * Determines if the question response is a valid essay response.
     *
     * @param response the essay response.
     * @return true if the response is valid, otherwise false.
     */
    @Override
    protected boolean performResponseValidation(String response) {
        boolean isValid;

        // Test for null or blank string.
        if (!(isValid = !Validation.isNullOrBlank(response)))
            SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

        return isValid;
    }
}
