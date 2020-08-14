package survey.question;

import menu.ModifyQuestionMenu;
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
        boolean isNullOrEmpty;

        // Get question type.
        String questionType = getQuestionType();

        do {
            // Record question prompt.
            SurveyApp.out.displayMenuPrompt("Enter the prompt for your " + questionType + " question:");
            prompt = SurveyApp.in.readQuestionPrompt();

            // Check if valid prompt.
            if (isNullOrEmpty = SurveyApp.isNullOrEmpty(prompt)) {
                SurveyApp.displayInvalidInputMessage("prompt");
            }
        } while (isNullOrEmpty);

        return prompt;
    }

    protected int getValidNumResponses() {
        return getValidNumResponses("responses");
    }

    protected int getValidNumResponses(String responseType) {
        Integer numResponses;
        boolean isValidNumResponses;

        // Get question type.
        String questionType = getQuestionType();

        do {
            // Record number of question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of " + responseType + " for your " + questionType + " question.");
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

    public abstract void modify();

    /**
     * Determine if the user would like to modify their question prompt and
     * if so set the new prompt.
     *
     * @return true if the user chose to return to the previous menu, otherwise false.
     */
    protected boolean modifyPrompt() {
        String choice, newPrompt;
        boolean isNullOrEmpty;
        boolean isReturn = false;

        // Display the current prompt.
        SurveyApp.out.displayMenuPrompt(prompt);

        // Get user choice.
        choice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_PROMPT, ModifyQuestionMenu.OPTIONS);

        switch (choice) {
            case ModifyQuestionMenu.YES:

                // Loop until user enters valid new prompt.
                do {
                    // Display the current prompt.
                    SurveyApp.out.displayMenuPrompt(prompt);

                    // Record question prompt.
                    SurveyApp.out.displayMenuPrompt("Enter a new prompt:");
                    newPrompt = SurveyApp.in.readQuestionPrompt();

                    // Check if valid prompt.
                    if (isNullOrEmpty = SurveyApp.isNullOrEmpty(newPrompt)) {
                        SurveyApp.displayInvalidInputMessage("prompt");
                    } else {
                        // Set the prompt.
                        prompt = newPrompt;
                    }
                } while (isNullOrEmpty);

                break;

            case ModifyQuestionMenu.RETURN:

                isReturn = true;
                break;

            default:
                break;
        }

        return isReturn;
    }

    protected boolean modifyNumResponses() {
        return modifyNumResponses("responses");
    }

    protected boolean modifyNumResponses(String responseType) {
        String choice;
        Integer newNumResponses;
        boolean isValidNumResponses;
        boolean isReturn = false;

        // Display the current prompt.
        SurveyApp.out.displayMenuPrompt("The current number of " + responseType + " is: " + numResponses);

        // Get user choice.
        choice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_NUM_RESPONSES, ModifyQuestionMenu.OPTIONS);

        switch (choice) {
            case ModifyQuestionMenu.YES:

                // Loop until user enters valid new number of responses.
                do {
                    // Record new number of question responses.
                    SurveyApp.out.displayMenuPrompt("Enter a new number of " + responseType + ":");
                    newNumResponses = SurveyApp.in.readQuestionChoiceCount();

                    // Check if valid new number of responses.
                    if (!(isValidNumResponses = isValidNumResponses(newNumResponses))) {
                        SurveyApp.displayInvalidInputMessage("number");
                    } else {
                        // Set the prompt.
                        numResponses = newNumResponses;
                    }
                } while (!isValidNumResponses);

                break;

            case ModifyQuestionMenu.RETURN:

                isReturn = true;
                break;

            default:
                break;
        }

        return isReturn;
    }
}
