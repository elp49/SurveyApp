package survey.question;

import menu.ModifyQuestionMenu;
import survey.SurveyApp;
import survey.response.QuestionResponse;
import utils.Validation;

import java.io.Serializable;

public abstract class Question implements Serializable {
    private static long serialVersionUID = 1L;
    protected String prompt;
    protected int numResponses;
    protected String questionType;
    protected String responseType;
    protected transient QuestionResponse questionResponse;

    public Question() {
        prompt = "";
        numResponses = 0;
        questionType = "Unknown Question Type";
        responseType = "response";
        questionResponse = null;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getQuestionType() {
        return questionType;
    }

    public abstract void create();

    protected String getValidPrompt() {
        String prompt;
        boolean isNullOrBlank;

        do {
            // Record question prompt.
            SurveyApp.out.displayMenuPrompt("Enter the prompt for your " + questionType + " question:");
            prompt = SurveyApp.in.readQuestionPrompt();

            // Check if valid prompt.
            if (isNullOrBlank = Validation.isNullOrBlank(prompt)) {
                SurveyApp.displayInvalidInputMessage("prompt");
            }
        } while (isNullOrBlank);

        return prompt;
    }

    protected int getValidNumResponses() {
        Integer numResponses;
        boolean isValidNumResponses;

        do {
            // Record number of question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of " + responseType + "s for your " + questionType + " question.");
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
        boolean isNullOrBlank;
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

                    // Test if valid prompt.
                    if (!(isNullOrBlank = Validation.isNullOrBlank(newPrompt)))
                        prompt = newPrompt;
                    else
                        SurveyApp.displayInvalidInputMessage("prompt");

                } while (isNullOrBlank);

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

    public QuestionResponse readQuestionResponse() {
        int i;
        String response;
        boolean isValidResponse;
        QuestionResponse qr;

        // Initialize question response object.
        questionResponse = new QuestionResponse();

        // Loop until user gives valid response(s).
        for (i = 0; i < numResponses; i++) {
            do {
                // Record user response.
                response = SurveyApp.in.readQuestionResponse();

                // Test response validity.
                isValidResponse = performResponseValidation(response);

            } while (!isValidResponse);

            // Add response string to question response.
            questionResponse.add(response);
        }

        // Clean up question response object.
        qr = questionResponse;
        questionResponse = null;

        return qr;
    }

    protected abstract boolean performResponseValidation(String response);
}
