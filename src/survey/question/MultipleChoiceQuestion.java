package survey.question;

import menu.ModifyQuestionMenu;
import survey.SurveyApp;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuestion extends Question {
    private int numResponses;
    private int numChoices;
    private ChoiceList choiceList;

    public MultipleChoiceQuestion() {
        super();
        numChoices = 0;
        choiceList = new ChoiceList();
    }

    public ChoiceList getChoiceList() {
        return choiceList;
    }

    @Override
    public String getQuestionType() {
        return "Multiple Choice";
    }

    @Override
    public String getResponseType() {
        return "choice(s)";
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
        return !SurveyApp.isNullOrEmpty(s);
    }

    protected int getValidNumChoices() {
        Integer numChoices;
        boolean isValidNumChoices;

        // Get survey.question type.
        String questionType = getQuestionType();

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

    protected int getValidNumResponses(int numChoices) {
        Integer numResponses;
        boolean isValidNumResponses;

        // Get survey.question type.
        String questionType = getQuestionType();

        do {
            // Record number of survey.question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of allowed " + getResponseType() + " for your " + questionType + " question.");
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
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayNote("Please give " + numResponses + " choice(s).", true);
        SurveyApp.out.displayQuestionChoiceList(choiceList, true);
    }

    @Override
    public void modify() {
        // Modify the question prompt. If return value is true,
        // then user chose to return to the previous menu.
        boolean isReturn = modifyPrompt();

        // Test return value.
        if (!isReturn) modifyNumResponses();

        // Test return value.
        if (!isReturn) modifyChoices();
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
        boolean isValidUserChoice;
        int choiceCharIndex, choiceIndex;

        // Get list of possible choice characters that could be entered.
        List<String> choiceChars = getPossibleChoiceCharacters();

        do {
            // Record choice character.
            choiceChar = SurveyApp.in.readQuestionChoice();

            // Check if valid choice.
            if (!(isValidUserChoice = isValidUserChoice(choiceChar, choiceChars))) {
                SurveyApp.displayInvalidInputMessage("choice");
            }
        } while (!isValidUserChoice);

        // Get index of the choice character.
        choiceCharIndex = choiceChars.indexOf(choiceChar);

        // Divide by 2 to get the index of the choice in choice list.
        if (choiceCharIndex == 0) choiceIndex = 0;
        else choiceIndex = choiceCharIndex / 2;

        // Return chosen choice index.
        return choiceIndex;
    }

    protected List<String> getPossibleChoiceCharacters() {
        char choiceCharUpper = 'A';
        char choiceCharLower = 'a';
        List<String> result = new ArrayList<>();

        // Create list of possible choice characters that could be entered.
        for (String s : choiceList.getChoices()) {
            // Add uppercase and lowercase choice characters to options list.
            result.add(Character.toString(choiceCharUpper));
            result.add(Character.toString(choiceCharLower));

            choiceCharUpper++;
            choiceCharLower++;
        }

        return result;
    }

    protected boolean isValidUserChoice(String choice, List<String> choiceChars) {
        boolean isValidUserChoice = false;

        // Test choice for null or empty string.
        // Test choiceChars if contains choice.
        if ((!SurveyApp.isNullOrEmpty(choice)))
            isValidUserChoice = choiceChars.contains(choice);

        return isValidUserChoice;
    }

    @Override
    public List<String> getValidResponseList() {
        int choiceIndex, i;
        List<String> responseList = new ArrayList<>();

        // Loop until user gives valid choice(s).
        for (i = 0; i < numResponses; i++) {
            // Get index of choice in choice list.
            choiceIndex = getValidUserChoiceIndex();

            // Add choice to response list.
            responseList.add(choiceList.get(choiceIndex));
        }

        return responseList;
    }
}
