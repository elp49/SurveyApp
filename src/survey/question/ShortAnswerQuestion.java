package survey.question;

import survey.SurveyApp;
import survey.response.QuestionResponse;
import utils.Validation;

import java.util.ArrayList;
import java.util.List;

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
    protected String readValidQuestionResponse() {
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

    @Override
    public void tabulate(List<QuestionResponse> questionResponseList) {
        int responseIndex, newCount, i;
        List<String> responseList;
        List<String> resultResponseList = new ArrayList<>();
        List<Integer> resultResponseCountList = new ArrayList<>();

        // Create result lists.
        for (QuestionResponse qr : questionResponseList) {
            // Get responses list.
            responseList = qr.getResponseList();

            // Add all responses to result response list.
            for (String s : responseList) {
                // Test is response is not already in result list.
                if (!resultResponseList.contains(s)) {
                    resultResponseList.add(s);
                    resultResponseCountList.add(1);
                } else {
                    // Get index of response in result list.
                    responseIndex = resultResponseList.indexOf(s);

                    // Get new response count.
                    newCount = resultResponseCountList.get(responseIndex) + 1;

                    // Increment response count.
                    resultResponseCountList.set(responseIndex, newCount);
                }
            }
        }

        // Display question prompt.
        SurveyApp.out.displayQuestion(prompt);

        for (i = 0; i < resultResponseList.size(); i++) {
            SurveyApp.out.displayQuestionResponse(resultResponseList.get(i) + " " + resultResponseCountList.get(i));
        }
    }

    @Override
    public QuestionResponse readCorrectAnswer() {
        int i;
        String answerPrompt, correctAnswer;
        QuestionResponse answerKey = new QuestionResponse();

        for (i = 1; i <= numResponses; i++) {
            // Create answer prompt.
            if (numResponses == 1)
                answerPrompt = "Enter the correct " + responseType + ":";
            else
                answerPrompt = "Enter correct " + responseType + " #" + i + ":";

            // Read a correct answer.
            SurveyApp.out.displayMenuPrompt(answerPrompt);
            correctAnswer = readValidQuestionResponse();

            // Add correct answer to answer key.
            answerKey.add(correctAnswer);
        }

        return answerKey;
    }

    @Override
    public void displayAnswer(QuestionResponse answer) {
        int i;

        // Test for single correct answer.
        if (numResponses == 1)
            SurveyApp.out.displayQuestionResponse(answer.get(0), false, true);

        else {
            for (i = 0; i < answer.size(); i++) {
                SurveyApp.out.displayQuestionResponse(answer.get(i));
            }
        }
    }
}
