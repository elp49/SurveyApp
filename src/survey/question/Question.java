package survey.question;

import survey.SurveyApp;

import java.io.Serializable;

public abstract class Question implements Serializable {
    private static long serialVersionUID = 1L;
    protected String prompt;
    protected int numResponses;

    public Question() {
        prompt = "";
        numResponses = 0;
    }

    public String getPrompt() {
        return prompt;
    }

    public int getNumResponses() {
        return numResponses;
    }

    public abstract String getQuestionType();

    public abstract void create();

    protected String getValidPrompt() {
        String prompt;

        // Get survey.question type.
        String questionType = getQuestionType();

        do {
            // Record survey.question prompt.
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
        return getValidNumResponses("responses");
    }

    protected int getValidNumResponses(String responseType) {
        Integer numResponses;
        boolean isValidNumResponses;

        // Get survey.question type.
        String questionType = getQuestionType();

        do {
            // Record number of survey.question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of required responses for your " + questionType + " question.");
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

    public abstract void display();
}
