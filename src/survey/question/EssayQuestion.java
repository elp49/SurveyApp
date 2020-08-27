package survey.question;

import survey.SurveyApp;
import survey.response.QuestionResponse;
import utils.Validation;

import java.util.List;

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
    public void modify(boolean isTest) {
        // Modify the question prompt. If return value is true,
        // then user chose to return to the previous menu.
        boolean isReturn = modifyPrompt();

        // Test return value.
        if (!isReturn && !isTest) modifyNumResponses();
    }

    @Override
    protected String readValidQuestionResponse() {
        boolean isValidResponse;
        String response;

        do {
            // Get question response.
            response = SurveyApp.in.readQuestionResponse();

            // Test for null or blank string.
            if (!(isValidResponse = !Validation.isNullOrBlank(response)))
                SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

            // If the user enters an invalid question response,
            // then isValidResponse will be false.
        } while (!isValidResponse);

        return response;
    }

    @Override
    public void tabulate(List<QuestionResponse> questionResponseList) {
        List<String> responseList;

        // Display question prompt.
        SurveyApp.out.displayQuestion(prompt);

        // Display all question response.
        for (QuestionResponse qr : questionResponseList) {
            // Get responses list.
            responseList = qr.getResponseList();

            // Display all responses.
            for (String s : responseList)
                SurveyApp.out.displayQuestionResponse(s);

            // Link break.
            SurveyApp.out.displayNote("", true);
        }
    }

    @Override
    public QuestionResponse readCorrectAnswer() {
        return null;
    }

    @Override
    public void displayAnswer(QuestionResponse answer) {
    }

    @Override
    public boolean isCorrectResponse(QuestionResponse key, QuestionResponse response) {
        return false;
    }
}
