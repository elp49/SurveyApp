package survey.question;

import menu.ModifyQuestionMenu;
import survey.SurveyApp;
import utils.Validation;

public class MultipleChoiceQuestion extends Question {
    protected int numChoices;
    protected ChoiceList choiceList;

    public MultipleChoiceQuestion() {
        super();
        questionType = "Multiple Choice";
        responseType = "choice";
        numChoices = 0;
        choiceList = new ChoiceList();
    }

    @Override
    public void create() {
        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of choices.
        numChoices = getNumChoices();

        // Get valid choice list.
        choiceList = getValidChoiceList();

        // Get valid number of responses.
        numResponses = getValidNumResponses();
    }

    /**
     * Get a valid number of choices. A valid number of choices is between
     * 1 and 26 inclusive.
     *
     * @return the valid number of choices
     */
    protected int getNumChoices() {
        Integer numChoices;
        boolean isValidNumChoices;

        do {
            // Record number of survey.question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of choices for your " + questionType + " question.");
            numChoices = SurveyApp.in.readQuestionChoiceCount();

            // Check if valid number of choices.
            if (!(isValidNumChoices = isValidNumChoices(numChoices))) {
                SurveyApp.displayInvalidInputMessage("number");
            }
        } while (!isValidNumChoices);

        return numChoices;
    }

    /**
     * Determine if the given number is a valid number of choices.
     * Valid numbers are between 1 and 26 inclusive.
     *
     * @param i the number of choices
     * @return true if it is a valid number of choices, otherwise false
     */
    protected boolean isValidNumChoices(Integer i) {
        return i != null && i > 1 && i <= 26;
    }

    protected ChoiceList getValidChoiceList() {
        int i;
        String choice;
        char choiceChar = 'A';
        ChoiceList result = new ChoiceList();

        for (i = 1; i <= numChoices; i++) {
            // Display question choice prompt.
            SurveyApp.out.displayMenuPrompt("Enter choice " + choiceChar + ").");

            // Get valid choice.
            choice = getValidChoiceText();

            // Add choice to choice list.
            result.add(choice);

            choiceChar++;
        }

        return result;
    }

    /**
     * Read user input until a valid choice is given.
     *
     * @return a valid choice
     */
    protected String getValidChoiceText() {
        String choice;
        boolean isValidChoiceText;

        do {
            // Record choice.
            choice = SurveyApp.in.readQuestionChoice();

            // Check if valid choice.
            if (!(isValidChoiceText = isValidChoiceText(choice))) {
                SurveyApp.displayInvalidInputMessage("choice");
            }
        } while (!isValidChoiceText);

        return choice;
    }

    /**
     * Determine if a choice is valid. A valid choice is not null or blank.
     *
     * @param s the choice
     * @return true if the choice is valid, otherwise false
     */
    protected boolean isValidChoiceText(String s) {
        return !Validation.isNullOrBlank(s);
    }

    @Override
    protected int getValidNumResponses() {
        Integer numResponses;
        boolean isValidNumResponses;

        do {
            // Record number of survey.question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of allowed " + responseType + "s for your " + questionType + " question.");
            numResponses = SurveyApp.in.readQuestionChoiceCount();

            // Check if valid number of responses.
            if (!(isValidNumResponses = isValidNumResponses(numResponses))) {
                SurveyApp.displayInvalidInputMessage("number");
            }
        } while (!isValidNumResponses);

        return numResponses;
    }

    @Override
    protected boolean isValidNumResponses(Integer i) {
        return i != null && i > 0 && i < numChoices;
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestion(new String[]{prompt,
                "Please give " + numResponses + " choice(s)."
        }, choiceList);
    }

    @Override
    public void modify() {
        // Modify the question prompt. If return value is true,
        // then user chose to return to the previous menu.
        boolean isReturn = modifyPrompt();

        // Test is user chose to return to previous menu.
        if (!isReturn) {
            // Modify a question choice.
            isReturn = modifyChoice();

            // Test return value.
            if (!isReturn)
                modifyNumResponses();
        }

    }

    /**
     * Determine if the user would like to modify their question choices.
     * If yes, then get and set the new question choices.
     *
     * @return true if the user chose to return to the previous menu, otherwise false.
     */
    protected boolean modifyChoice() {
        int choiceIndex;
        String choiceChar, newChoice;
        boolean isReturn;

        // Determine if user will modify question choices.
        String menuChoice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_CHOICES, ModifyQuestionMenu.OPTIONS);

        // Test if user chose to return to previous menu.
        if (!(isReturn = menuChoice.equals(ModifyQuestionMenu.RETURN))) {
            // Display choice list.
            SurveyApp.out.displayQuestion(new String[]{
                    "Which choice do you want to modify?",
                    "Enter the character."
            }, choiceList);

            // Get choice character of one of the possible choices to be modified.
            choiceChar = readPossibleQuestionResponse();

            // Get index of the choice.
            choiceIndex = getChoiceCharIndex(choiceChar.charAt(0));

            // Get valid new question choice.
            SurveyApp.out.displayMenuPrompt("Enter the new choice " + choiceChar + ").");
            newChoice = getValidChoiceText();

            // Update choice list with modified choice.
            setChoice(choiceIndex, newChoice);
        }

        return isReturn;
    }

    @Override
    public String readPossibleQuestionResponse() {
        String response;
        boolean isPossibleResponse;

        do {
            // Get user choice.
            response = SurveyApp.in.readQuestionResponse().toUpperCase();

            // Test for null or blank choice.
            if (!(isPossibleResponse = !Validation.isNullOrBlank(response)))
                SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

                // Test if response is more than a single character
            else if (!(isPossibleResponse = (response.length() == 1)))
                SurveyApp.out.displayNote("You must enter one character at a time.");

                // Test if choice is a possible choice given the choice list.
            else if (!(isPossibleResponse = isPossibleChoiceChar(response.charAt(0))))
                SurveyApp.out.displayNote(response + " is not a valid " + responseType);

            // If the user enters an impossible choice,
            // then isPossibleResponse will be false.
        } while (!isPossibleResponse);

        return response;
    }

    /**
     * Determine if the choice character is possible given the choice list.
     *
     * @param choiceChar the choice character to be tested
     * @return true if the choice is a possible choice, otherwise false
     */
    protected boolean isPossibleChoiceChar(char choiceChar) {
        boolean isPossibleChoiceChar;

        // Get choice character index.
        int choiceIndex = getChoiceCharIndex(choiceChar);

        // Test choice character index is in range of possible choices.
        isPossibleChoiceChar = Validation.isInRange(choiceIndex, 0, choiceList.size() - 1);

        return isPossibleChoiceChar;
    }

    /**
     * Parse the choice character index.
     *
     * @param choiceChar the choice character index to be parsed
     * @return the parsed choice character index or null
     */
    protected int getChoiceCharIndex(char choiceChar) {
        return choiceChar - 65;
    }

    /**
     * Set the choice at the specified position.
     *
     * @param choiceIndex the index of the choice in the choice list
     * @param choice      the new choice
     * @return the choice previously at the specified position
     */
    protected String setChoice(int choiceIndex, String choice) {
        return choiceList.set(choiceIndex, choice);
    }

    @Override
    protected void modifyNumResponses() {
        Integer newNumResponses;
        boolean isValidNumResponses;

        // Get user choice.
        String choice = SurveyApp.getUserMenuChoice(new String[]{
                ModifyQuestionMenu.MODIFY_NUM_RESPONSES,
                "The current number of allowed " + responseType + "s is: " + numResponses
        }, ModifyQuestionMenu.OPTIONS);

        // Test if user chose to return to previous menu.
        if (!choice.equals(ModifyQuestionMenu.RETURN)) {
            // Loop until user enters valid new number of responses.
            do {
                // Record new number of question responses.
                SurveyApp.out.displayMenuPrompt("Enter a new number of allowed " + responseType + "s.");
                newNumResponses = SurveyApp.in.readQuestionChoiceCount();

                // Test if new number of responses is invalid.
                if (!(isValidNumResponses = isValidNumResponses(newNumResponses)))
                    SurveyApp.displayInvalidInputMessage("number");

                // If user enters an invalid new number of response,
                // then isValidNumResponses will be false.
            } while (!isValidNumResponses);

            // Set the new number of responses.
            numResponses = newNumResponses;
        }
    }

    @Override
    protected boolean isValidResponse(String response) {
        boolean isValidResponse = true;

        // Loop through existing user matches.
        for (String existingChoice : questionResponse.getResponseList()) {
            // Test if choice has already been recorded.
            if (!(isValidResponse = !response.equals(existingChoice))) {
                SurveyApp.out.displayNote("You've already entered choice " + response);
                break;
            }
        }

        return isValidResponse;
    }
}
