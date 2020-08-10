package survey.question;

import survey.SurveyApp;

import java.io.Serializable;

public abstract class Question implements Serializable {
    private static long serialVersionUID = 1L;
    protected String prompt;
    protected int numResponses;

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

            // Check if valid prompt.
            if (SurveyApp.isNullOrEmpty(prompt)) {
                SurveyApp.displayInvalidInputMessage("prompt");
            }
        } while (SurveyApp.isNullOrEmpty(prompt));

        return prompt;
    }

    protected int getValidNumResponses() {
        Integer numResponses;
        boolean isValidNumResponses;

        // Get question type.
        String questionType = getQuestionType();

        do {
            // Record number of question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of allowed responses for your " + questionType + " question.");
            numResponses = SurveyApp.in.readQuestionChoiceCount();

            // Check if valid number of responses.
            if (!(isValidNumResponses = isValidNumResponses(numResponses))) {
                SurveyApp.displayInvalidInputMessage("number");
            }
        } while (!isValidNumResponses);

        return numResponses;
    }

    protected boolean isValidNumResponses(Integer i) {
        return i != null && i > 0;
    }
}
