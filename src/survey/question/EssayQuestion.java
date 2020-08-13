package survey.question;

import survey.SurveyApp;

public class EssayQuestion extends Question {

    public EssayQuestion() {
        super();
    }

    @Override
    public void create() {
        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of responses.
        numResponses = getValidNumResponses();
    }

    @Override
    public String getQuestionType() {
        return "Essay";
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayNote("Please give " + numResponses + " responses.");
    }
}
