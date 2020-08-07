package survey.question;

import survey.Survey;
import survey.SurveyApp;

import java.io.Serializable;

public abstract class Question implements Serializable {
    private static long serialVersionUID = 1L;
    private String prompt;
    private int numResponses;

    public Question() { }

    public String getPrompt() {
        return prompt;
    }

    public int getNumResponses() {
        return numResponses;
    }

    public abstract String getQuestionType();

    public void create() {
        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of responses.
        numResponses = getValidNumResponses();
    }

    protected String getValidPrompt() {
        String prompt;

        // Get question type.
        String questionType = getQuestionType();

        do {
            // Record question prompt.
            SurveyApp.out.displayMenuPrompt("Enter the prompt for your " + questionType + " question:");
            prompt = SurveyApp.in.readQuestionPrompt();

            if (SurveyApp.isNullOrEmpty(prompt)) {
                SurveyApp.displayInvalidInputMessage("prompt");
            }
        } while (SurveyApp.isNullOrEmpty(prompt));

        return prompt;
    }

    protected int getValidNumResponses() {
        Integer numResponses;

        // Get question type.
        String questionType = getQuestionType();

        do {
            // Record number of question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of allowed responses for your " + questionType + " question.");
            numResponses = SurveyApp.in.readQuestionChoiceCount();

            if (!isValidNumResponses(numResponses)) {
                SurveyApp.displayInvalidInputMessage("number");
            }
        } while (!isValidNumResponses(numResponses));

        return numResponses;
    }

    protected boolean isValidNumResponses(Integer i) {
        return i != null && i > 0;
    }
}
