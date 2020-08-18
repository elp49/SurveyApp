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
        numChoices = getValidNumChoices();

        // Get valid choice list.
        choiceList = getValidChoiceList();

        // Get valid number of responses.
        numResponses = getValidNumResponses(numChoices);
    }

    protected ChoiceList getValidChoiceList() {
        int i;
        String choice;
        ChoiceList result = new ChoiceList();

        for (i = 1; i <= numChoices; i++) {
            // Display question choice prompt.
            SurveyApp.out.displayMenuPrompt("Enter choice " + i + ").");

            // Get valid choice.
            choice = getValidChoice();

            // Add choice to choice list.
            result.add(choice);
        }

        return result;
    }

    /**
     * Read user input until a valid choice string is given.
     *
     * @return the valid choice string.
     */
    protected String getValidChoice() {
        String choice;
        boolean isValidChoice;

        do {
            // Record choice.
            choice = SurveyApp.in.readQuestionChoice();

            // Check if valid choice.
            if (!(isValidChoice = isValidChoice(choice))) {
                SurveyApp.displayInvalidInputMessage("choice");
            }
        } while (!isValidChoice);

        return choice;
    }

    protected boolean isValidChoice(String s) {
        return !Validation.isNullOrBlank(s);
    }

    protected int getValidNumChoices() {
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

    protected boolean isValidNumChoices(Integer i) {
        return i != null && i > 1 && i <= 26;
    }

    @Override
    protected int getValidNumResponses() {
        return getValidNumResponses(numChoices);
    }

    protected int getValidNumResponses(int numChoices) {
        Integer numResponses;
        boolean isValidNumResponses;

        do {
            // Record number of survey.question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of allowed " + responseType + "s for your " + questionType + " question.");
            numResponses = SurveyApp.in.readQuestionChoiceCount();

            // Check if valid number of responses.
            if (!(isValidNumResponses = isValidNumResponses(numResponses, numChoices))) {
                SurveyApp.displayInvalidInputMessage("number");
            }
        } while (!isValidNumResponses);

        return numResponses;
    }

    @Override
    protected boolean isValidNumResponses(Integer i) {
        return i != null && i > 0 && i < numChoices;
    }

    protected boolean isValidNumResponses(Integer i, Integer numChoices) {
        return i != null && i > 0 && i < numChoices;
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestionPrompt(new String[]{
                prompt,
                "Please give " + numResponses + " choice(s)."
        });
        SurveyApp.out.displayQuestionChoiceList(choiceList, true);
    }

    @Override
    public void modify() {
        // Modify the question prompt. If return value is true,
        // then user chose to return to the previous menu.
        boolean isReturn = modifyPrompt();

        // Test return value.
        if (!isReturn) isReturn = modifyChoices();

        // Test return value.
        if (!isReturn) modifyNumResponses();
    }

    @Override
    protected boolean modifyNumResponses() {
        String choice;
        Integer newNumResponses;
        boolean isValidNumResponses;
        boolean isReturn = false;

        // Display the current prompt.
        SurveyApp.out.displayMenuPrompt("The current number of allowed " + responseType + "s is: " + numResponses);

        // Get user choice.
        choice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_NUM_RESPONSES, ModifyQuestionMenu.OPTIONS);

        switch (choice) {
            case ModifyQuestionMenu.YES:

                // Loop until user enters valid new number of responses.
                do {
                    // Record new number of question responses.
                    SurveyApp.out.displayMenuPrompt("Enter a new number of allowed " + responseType + "s:");
                    newNumResponses = SurveyApp.in.readQuestionChoiceCount();

                    // Check if valid new number of responses.
                    if (!(isValidNumResponses = isValidNumResponses(newNumResponses, choiceList.size()))) {
                        SurveyApp.displayInvalidInputMessage("number");
                    } else {
                        // Set the number of allowed choices.
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

    /**
     * Determine if the user would like to modify their question choices.
     * If yes, then get and set the new question choices.
     *
     * @return true if the user chose to return to the previous menu, otherwise false.
     */
    protected boolean modifyChoices() {
        int choiceIndex;
        String newChoice;
        boolean isReturn = false;

        // Determine if user will modify question choices.
        String choice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_CHOICES, ModifyQuestionMenu.OPTIONS);

        switch (choice) {
            case ModifyQuestionMenu.YES:

                // Display choice list.
                SurveyApp.out.displayMenuPrompt("Which choice do you want to modify?");
                SurveyApp.out.displayQuestionChoiceList(choiceList);

                // Get index of choice in choice list to be modified.
                choiceIndex = getValidUserChoiceIndex();

                // Get valid new question choice.
                SurveyApp.out.displayMenuPrompt("Enter the new choice:");
                newChoice = getValidChoice();

                // Update choice list with modified choice.
                choiceList.set(choiceIndex, newChoice);

                break;

            case ModifyQuestionMenu.RETURN:

                isReturn = true;
                break;

            default:
                break;
        }

        return isReturn;
    }

    /**
     * Get a valid user choice from choice list.
     *
     * @return the valid user choice.
     */
    protected int getValidUserChoiceIndex() {
        String choiceChar;
        int choiceIndex;
        int A = 65;

        do {
            // Get a valid choice character.
            choiceChar = getValidChoiceCharacter();
        } while (Validation.isNullOrBlank(choiceChar));

        // Convert choice character to choice index.
        choiceIndex = choiceChar.charAt(0) - A;

        // Return chosen choice index.
        return choiceIndex;
    }

    /**
     * Read user response and determine if it is a valid choice character.
     * If it is not, then report why.
     *
     * @return the valid choice character or null if it is invalid
     */
    protected String getValidChoiceCharacter() {
        String result;
        int choiceIndex;
        int A = 65;

        // Record user choice character.
        String choiceChar = SurveyApp.in.readQuestionResponse();

        // Test for null or blank string.
        if (!Validation.isNullOrBlank(choiceChar)) {
            SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");
            return null;
        }

        // Test character is alphabetic letter.
        if (!Validation.isAlphabeticLetter(choiceChar)) {
            SurveyApp.out.displayNote("Your choice should be a letter.");
            return null;
        }

        // Test choice is only one character.
        else if (choiceChar.length() != 1) {
            SurveyApp.out.displayNote("Please enter one character at a time.");
            return null;
        }

        // Get uppercase character.
        result = choiceChar.toUpperCase();

        // Convert choice character to choice index.
        choiceIndex = result.charAt(0) - A;

        // Test choice index is in range of choice list.
        if (!Validation.isInRange(choiceIndex, 0, choiceList.size())) {
            SurveyApp.out.displayNote("Please enter one of the available choices.");
            return null;
        }

        return result;
    }

    /**
     * Determines if the multiple choice response is valid. If it is invalid, report why.
     *
     * @return the response string
     */
    protected String getValidResponse() {
        // Get a valid choice character.
        String choiceChar = getValidChoiceCharacter();

        // Test choice has not already been chosen.
        if (questionResponse.contains(choiceChar)) {
            SurveyApp.out.displayNote("You have already entered choice " + choiceChar);
            return null;
        }

        return choiceChar;
    }
}
