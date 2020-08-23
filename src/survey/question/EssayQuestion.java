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
    public void modify() {
        // Modify the question prompt. If return value is true,
        // then user chose to return to the previous menu.
        boolean isReturn = modifyPrompt();

        // Test return value.
        if (!isReturn) modifyNumResponses();
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

            // If the user enters an impossible question response,
            // then isPossibleResponse will be false.
        } while (!isPossibleResponse);

        return response;
    }

    @Override
    protected boolean isValidResponse(String response) {
        return true;
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
            SurveyApp.out.displayNote("");
        }
    }
}
