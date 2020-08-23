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
        boolean isValidPrompt;

        do {
            // Record question prompt.
            SurveyApp.out.displayMenuPrompt("Enter the prompt for your " + questionType + " question:");
            prompt = SurveyApp.in.readQuestionPrompt();

            // Test if prompt is invalid.
            if (!(isValidPrompt = !Validation.isNullOrBlank(prompt)))
                SurveyApp.displayInvalidInputMessage("prompt");

            // If the prompt is invalid,
            // then isValidPrompt will be false.
        } while (!isValidPrompt);

        return prompt;
    }

    protected int getValidNumResponses() {
        Integer numResponses;
        boolean isValidNumResponses;

        do {
            // Record number of question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of " + responseType + "s for your " + questionType + " question.");
            numResponses = SurveyApp.in.readQuestionChoiceCount();

            // Test number of responses is invalid.
            if (!(isValidNumResponses = isValidNumResponses(numResponses)))
                SurveyApp.displayInvalidInputMessage("number");

            // If the number of responses is invalid,
            // then isValidNumResponses will be false.
        } while (!isValidNumResponses);

        return numResponses;
    }

    protected boolean isValidNumResponses(Integer i) {
        return i != null && i > 0;
    }

    public abstract void display();

    public abstract void modify();

    /**
     * Determine if the user would like to modify their question prompt.
     * If yes, then get and set the new prompt.
     *
     * @return true if the user chose to return to the previous menu, otherwise false.
     */
    protected boolean modifyPrompt() {
        String newPrompt;
        boolean isValidPrompt;
        boolean isReturn = false;

        // Display the current prompt.
        SurveyApp.out.displayMenuPrompt(prompt);

        // Get user choice.
        switch (SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_PROMPT, ModifyQuestionMenu.OPTIONS)) {
            // Test if user chose to modify prompt.
            case ModifyQuestionMenu.YES:
                // Loop until user enters valid new prompt.
                do {
                    // Display the current prompt.
                    SurveyApp.out.displayMenuPrompt(prompt);

                    // Record question prompt.
                    SurveyApp.out.displayMenuPrompt("Enter a new prompt:");
                    newPrompt = SurveyApp.in.readQuestionPrompt();

                    // Test if new prompt is invalid.
                    if (!(isValidPrompt = !Validation.isNullOrBlank(newPrompt)))
                        SurveyApp.displayInvalidInputMessage("prompt");

                    // If the new prompt is invalid,
                    // then isValidPrompt will be false.
                } while (!isValidPrompt);

                // Set the new prompt.
                prompt = newPrompt;
                break;

            // Test if user chose to return to previous menu.
            case ModifyQuestionMenu.RETURN:
                isReturn = true;
                break;

            default:
                break;
        }

        return isReturn;
    }

    protected void modifyNumResponses() {
        String choice;
        Integer newNumResponses;
        boolean isValidNumResponses;

        // Display the current prompt.
        SurveyApp.out.displayMenuPrompt("The current number of " + responseType + "(s) is: " + numResponses);

        // Get user choice.
        choice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_NUM_RESPONSES, ModifyQuestionMenu.OPTIONS);

        // Test if user chose to modify number of responses.
        if (choice.equals(ModifyQuestionMenu.YES)) {
            // Loop until user enters valid new number of responses.
            do {
                // Record new number of question responses.
                SurveyApp.out.displayMenuPrompt("Enter a new number of " + responseType + ":");
                newNumResponses = SurveyApp.in.readQuestionChoiceCount();

                // Test new number of responses is invalid.
                if (!(isValidNumResponses = isValidNumResponses(newNumResponses)))
                    SurveyApp.displayInvalidInputMessage("number");

                // If the new number of responses is invalid,
                // then isValidNumResponses will be false.
            } while (!isValidNumResponses);

            // Set the number of responses.
            numResponses = newNumResponses;
        }
    }

    /**
     * Read the question response from user input. Some question responses
     * will contain a multiple number of responses. Use these responses to
     * create a question response object.
     *
     * @return the question response object
     */
    public QuestionResponse readQuestionResponse() {
        int i;
        String response;
        QuestionResponse qr;

        // Initialize question response object.
        questionResponse = new QuestionResponse();

        // Loop until user gives valid response(s).
        for (i = 0; i < numResponses; i++) {
            do {
                // Record user response.
                response = readPossibleQuestionResponse();

                // Test if valid response.
            } while (!isValidResponse(response));

            // Add response string to question response.
            questionResponse.add(response);
        }

        // Clean up question response object.
        qr = questionResponse;
        questionResponse.clear();

        return qr;
    }

    /**
     * Read a possible question response.
     *
     * @return a possible choice
     */
    protected abstract String readPossibleQuestionResponse();

    /**
     * Determine if the provided question response is valid.
     *
     * @param response the question response
     * @return true if the question response is valid, otherwise false
     */
    protected abstract boolean isValidResponse(String response);
}
