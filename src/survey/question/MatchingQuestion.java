package survey.question;

import menu.ModifyQuestionMenu;
import survey.SurveyApp;
import utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class MatchingQuestion extends Question {
    protected final int numChoiceLists = 2;
    protected List<ChoiceList> choiceSet;

    public MatchingQuestion() {
        super();
        questionType = "Matching";
        responseType = "match";
        choiceSet = new ArrayList<>();
    }

    @Override
    public void create() {
        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of matches.
        numResponses = getValidNumResponses();

        // Build valid matching choice set.
        choiceSet = getValidChoiceSet();
    }

    @Override
    protected boolean isValidNumResponses(Integer i) {
        return i != null && i > 1;
    }

    /**
     * Get a valid choice set containing a number of choice lists
     * equivalent to numChoiceLists.
     *
     * @return a valid choice set
     */
    private List<ChoiceList> getValidChoiceSet() {
        int i;
        ChoiceList choiceList;
        List<ChoiceList> result = new ArrayList<>();

        for (i = 1; i <= numChoiceLists; i++) {
            SurveyApp.out.displayNote("Choice List #" + i);

            // Get valid choice list.
            choiceList = getValidChoiceList();

            // Add choice list to choice set.
            result.add(choiceList);
        }

        return result;
    }

    /**
     * Get a valid choice list containing a number of choices
     * equivalent to numResponses.
     *
     * @return a valid choice list
     */
    protected ChoiceList getValidChoiceList() {
        int i;
        String choice;
        ChoiceList result = new ChoiceList();

        for (i = 1; i <= numResponses; i++) {
            // Display survey.question choice prompt.
            SurveyApp.out.displayMenuPrompt("Enter choice " + i + ").");

            // Get valid choice.
            choice = getValidChoice();

            // Add choice to choice list.
            result.add(choice);
        }

        return result;
    }

    /**
     * Read user input until a valid choice is provided.
     *
     * @return a valid choice
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

            // If the choice is invalid, the isValidChoice will be false.
        } while (!isValidChoice);

        return choice;
    }

    /**
     * Determine if the given choice is valid, null, or blank.
     *
     * @param choice the choice to be validated
     * @return true if the choice is not null or blank, otherwise false
     */
    protected boolean isValidChoice(String choice) {
        return !Validation.isNullOrBlank(choice);
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestion(prompt, choiceSet);
    }

    @Override
    public void modify() {
        // Modify the question prompt. If return value is true,
        // then user chose to return to the previous menu.
        boolean isReturn = modifyPrompt();

        // Test return value.
        if (!isReturn) modifyChoiceSet();
    }

    /**
     * Determine if the user will modify a choice. If they will,
     * then determine which choice is to modified and get and
     * set the new choice.
     *
     * @return false if the user decides to return to the previous
     * menu, otherwise true
     */
    protected boolean modifyChoiceSet() {
        boolean isReturn, choiceWasModified;

        // Determine if user will modify question choices.
        String choice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_CHOICES, ModifyQuestionMenu.OPTIONS);

        // Test if the user chose to return to the previous menu.
        if (!(isReturn = choice.equals(ModifyQuestionMenu.RETURN))) {
            // Loop until user enters new valid choice.
            do {
                // Display choice set.
                SurveyApp.out.displayQuestion(new String[]{
                        "Which choice do you want to modify?",
                        "Enter the character or number."
                }, choiceSet);

                // Get and set the choice to be modified.
                choiceWasModified = modifyChoice();

                // If the user enters an impossible choice character or number,
                // then choiceWasModified will be false.
            } while (!choiceWasModified);
        }

        return isReturn;
    }

    /**
     * Get the choice the user will modify and determine which choice list
     * it is in. If the user enters a possible choice, then get and set
     * the new choice.
     *
     * @return true if the user enters a possible choice, otherwise false
     */
    protected boolean modifyChoice() {
        boolean isPossibleChoice;

        // Get choice to be modified.
        String response = SurveyApp.in.readQuestionResponse();

        // Determine if character or number choice.
        if (isPossibleChoice = isPossibleChar(response))
            modifyColumnOneChoice(response);
        else if (isPossibleChoice = isPossibleNumber(response))
            modifyColumnTwoChoice(response);
        else
            SurveyApp.out.displayNote(response + " is not a valid choice.");

        return isPossibleChoice;
    }

    /**
     * Get and set the new column one choice.
     *
     * @param choiceChar the choice character
     * @return the choice previously at the specified position
     */
    protected String modifyColumnOneChoice(String choiceChar) {
        // Get index of choice character.
        int choiceIndex = getChoiceCharIndex(choiceChar);

        // Get the new choice to be set.
        String newChoice = getNewValidChoice();

        // Set the new choice.
        return setChoice(newChoice, 0, choiceIndex);
    }

    /**
     * Get and set the new column two choice.
     *
     * @param choiceNum the choice number
     * @return the choice previously at the specified position
     */
    protected String modifyColumnTwoChoice(String choiceNum) {
        // Get index of choice number.
        int choiceIndex = getChoiceNumIndex(choiceNum);

        // Get the new choice to be set.
        String newChoice = getNewValidChoice();

        // Set the new choice.
        return setChoice(newChoice, 1, choiceIndex);
    }

    /**
     * Get a new valid choice.
     *
     * @return the new valid choice
     */
    protected String getNewValidChoice() {
        String choice;

        // Get valid new question choice.
        SurveyApp.out.displayMenuPrompt("Enter the new choice:");
        choice = getValidChoice();

        return choice;
    }

    /**
     * Set the choice at the specified position.
     *
     * @param choiceListIndex the index of the choice list in the choice set
     * @param choiceIndex     the index of the choice in the choice list
     * @return the choice previously at the specified position
     */
    protected String setChoice(String choice, int choiceListIndex, int choiceIndex) {
        return choiceSet.get(choiceListIndex).set(choiceIndex, choice);
    }

    /**
     * Read possible matches from user input and test if either choice
     * in the match has been entered already.
     *
     * @return a valid match
     */
    public String getValidResponse() {
        boolean isValidMatch;
        String validMatch;

        do {
            // Get a possible match.
            validMatch = getPossibleMatch();

            // Test if choices have been entered already.
            isValidMatch = isValidResponse(validMatch);

            // If user enters an impossible match or if one of the choices has
            // already been entered, then isValidMatch will be false.
        } while (!isValidMatch);

        return validMatch;
    }

    /**
     * Read user input and test if it is a possible match.
     * For it to be possible, both the entered character and number
     * must be within range of the available choices.
     *
     * @return a possible match
     */
    protected String getPossibleMatch() {
        boolean isPossibleMatch;
        String response;
        String[] possibleMatch;

        // Loop until user enters a possible match.
        do {
            // Get user's match.
            response = SurveyApp.in.readQuestionResponse().toUpperCase();

            // Test for null or blank response.
            if (!(isPossibleMatch = !Validation.isNullOrBlank(response))) {
                SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");
            } else {
                // Split the match.
                possibleMatch = splitMatch(response);

                // Test choice character index is in range of choice list.
                if (!(isPossibleMatch = isPossibleChar(possibleMatch[0]))) {
                    SurveyApp.out.displayNote(possibleMatch[0] + " is not a valid choice.");
                } else if (!(isPossibleMatch = isPossibleNumber(possibleMatch[1]))) {
                    SurveyApp.out.displayNote(possibleMatch[1] + " is not a valid choice.");
                }
            }

            // If user response is null or blank or either the choice or number
            // are invalid, then isPossibleMatch will be false.
        } while (!isPossibleMatch);

        return response;
    }

    /**
     * Parse each column and separate the given match string into an array.
     *
     * @param match the match to be split
     * @return the split match or null if match is null or blank
     */
    protected String[] splitMatch(String match) {
        String[] result = null;

        // Test match for null or blank.
        if (!Validation.isNullOrBlank(match)) {
            result = new String[]{
                    match.substring(0, 1),
                    match.substring(1)
            };
        }

        return result;
    }

    /**
     * Determine if the character is a possible choice given the choice set.
     *
     * @param c the character to be tested
     * @return true if the character is a possible choice, otherwise false
     */
    protected boolean isPossibleChar(String c) {
        boolean isPossibleChar;

        // Get choice character index.
        int choiceIndex = getChoiceCharIndex(c);

        // Test choice character index is in range of possible choices.
        isPossibleChar = Validation.isInRange(choiceIndex, 0, choiceSet.get(0).size() - 1);

        return isPossibleChar;
    }

    /**
     * Determine if the number is a possible choice given the choice set.
     *
     * @param n the number to be tested
     * @return true if the number is a possible choice, otherwise false
     */
    protected boolean isPossibleNumber(String n) {
        boolean isPossibleNumber = false;

        // Get choice number index.
        Integer num = getChoiceNumIndex(n);

        // Test number is in range of possible choices.
        if (num != null)
            isPossibleNumber = Validation.isInRange(num, 1, choiceSet.get(1).size());

        return isPossibleNumber;
    }

    /**
     * Parse the choice character index.
     *
     * @param choiceChar the choice character index to be parsed
     * @return the parsed choice character index or null
     */
    protected int getChoiceCharIndex(String choiceChar) {
        return choiceChar.charAt(0) - 65;
    }

    /**
     * Parse the choice number index.
     *
     * @param n the choice number string to be parse
     * @return the parsed choice number index or null
     */
    protected Integer getChoiceNumIndex(String n) {
        Integer num;

        try {
            num = Integer.parseInt(n) - 1;
        } catch (NumberFormatException ignore) {
            num = null;
        }

        return num;
    }

    /**
     * Determine if either choice within the possible match have already been chosen.
     *
     * @param possibleMatch the possible match
     * @return true if neither of the choices in the possible match have been
     *          chosen yet, otherwise false
     */
    @Override
    protected boolean isValidResponse(String possibleMatch) {
        int i;
        boolean isValidMatch;
        String[] matchArray, existingMatch;

        // Test possible match for null or blank.
        if (isValidMatch = !Validation.isNullOrBlank(possibleMatch)) {
            // Split the possible match.
            matchArray = splitMatch(possibleMatch);

            // Test match array for null.
            if (isValidMatch = (matchArray != null)) {
                // Loop through existing user matches.
                for (String s : questionResponse.getResponseList()) {
                    // Split the existing user match.
                    existingMatch = splitMatch(s);

                    // Test if either choice from the new match has already been recorded.
                    for (i = 0; i < matchArray.length; i++)
                        if (!(isValidMatch = !matchArray[i].equals(existingMatch[i]))) {
                            SurveyApp.out.displayNote("You've already entered choice " + matchArray[i]);
                            break;
                        }

                    // If one of the choices have already been entered,
                    // then isValidMatch will be false.
                    if (!isValidMatch)
                        break;
                }
            }
        }


        return isValidMatch;
    }
}
