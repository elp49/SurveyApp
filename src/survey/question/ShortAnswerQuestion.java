package survey.question;

import survey.SurveyApp;

public class ShortAnswerQuestion extends EssayQuestion {
    protected final int responseCharLimit = 64;

    public ShortAnswerQuestion() {
        super();
    }

    @Override
    public String getQuestionType() {
        return "Short Answer";
    }

    public int getResponseCharLimit() {
        return responseCharLimit;
    }

    @Override
    public void create() {
        SurveyApp.out.displayNote("Answers will be limited to " + responseCharLimit + " characters.");

        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of responses.
        numResponses = getValidNumResponses("answers");
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayNote("Limit your response to " + responseCharLimit + " characters.");
        SurveyApp.out.displayNote("Please give " + numResponses + " answers.");
    }
}
