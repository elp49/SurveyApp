package survey.question;

import menu.ModifyQuestionMenu;
import survey.SurveyApp;
import utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class MatchingQuestion extends Question {
    protected final String SEPARATOR = ":";
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

    @Override
    public void display() {
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayQuestionChoiceSet(choiceSet, true);
    }

    @Override
    public void modify() {
        // Modify the question prompt. If return value is true,
        // then user chose to return to the previous menu.
        boolean isReturn = modifyPrompt();

        // Test return value.
        if (!isReturn) modifyChoices();
    }

    protected boolean modifyChoices() {
        int choiceListIndex, choiceIndex;
        String newChoice, response;
        String[] match;
        boolean isReturn = false;

        // Determine if user will modify question choices.
        String choice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_CHOICES, ModifyQuestionMenu.OPTIONS);

        switch (choice) {
            case ModifyQuestionMenu.YES:
                /*// Display choice set.
                SurveyApp.out.displayQuestionPrompt("Which choice do you want to modify?");
                SurveyApp.out.displayQuestionChoiceSet(choiceSet, true);

                // Get the choice to be modified.
                response = getPossibleMatch();

                // Split the choices.
                match = splitMatch(response);

                */

                // Get index of choice in options list.
                choiceListIndex = getChoiceListIndex();

                // Get index of choice in choice list.
                choiceIndex = getChoiceIndex(choiceListIndex);

                // Get valid new question choice.
                SurveyApp.out.displayMenuPrompt("Enter the new choice:");
                newChoice = getValidChoice();

                // Set the new choice in chosen choice list.
                choiceSet.get(choiceListIndex).set(choiceIndex, newChoice);

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
     * Get the index of the choice list that the user will modify.
     *
     * @return the index of the choice list.
     */
    protected int getChoiceListIndex() {
        int i;
        String choice;
        List<String> options = new ArrayList<>();

        // Create list of possible columns to be modified.
        for (i = 1; i <= choiceSet.size(); i++)
            options.add("Column #" + i);

        // Get choice list to be modified.
        choice = SurveyApp.getUserMenuChoice("Which column of choices do you want to modify?", options);

        // Return index of choice in options list.
        return options.indexOf(choice);
    }

    /**
     * Get the index of the choice that the user will modify.
     *
     * @return the index of the choice.
     */
    protected int getChoiceIndex(int choiceListIndex) {
        String choice;
        List<String> choiceList;

        // Get chosen choice list.
        choiceList = choiceSet.get(choiceListIndex).getChoices();

        // Get choice to be modified from chosen choice list.
        choice = SurveyApp.getUserMenuChoice("Which choices do you want to modify in column #" + (choiceListIndex + 1) + "?", choiceList);

        // Return index of choice in choice list.
        return choiceList.indexOf(choice);
    }

    public String getValidResponse() {
        boolean isValidMatch;
        String newMatch;

        do {
            // Get a possible match.
            newMatch = getPossibleMatch();

            // Test match for null.
            isValidMatch = isValidMatch(newMatch);

            // If user enters an impossible match or if one of the choices has
            // already been entered, then isValidMatch will be false.
        } while (!isValidMatch);

        return newMatch;
    }

    protected String getPossibleMatch() {
        boolean isValid;
        String response;
        String[] match;

        do {
            // Get user matching response.
            response = SurveyApp.in.readQuestionResponse();

            // Test for null or blank response.
            if (!(isValid = !Validation.isNullOrBlank(response))) {
                SurveyApp.displayInvalidInputMessage(responseType);
            } else {
                // Split the match.
                match = splitMatch(response);

                // Test choice character index is in range of choice list.
                if (!(isValid = isPossibleChar(match[0]))) {
                    SurveyApp.out.displayNote(match[0] + " is not an available choice.");
                } else if (!(isValid = isPossibleNumber(match[1]))) {
                    SurveyApp.out.displayNote(match[1] + " is not an available choice.");
                }
            }
        } while (!isValid);

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
     * Parse the choice character index.
     *
     * @param choiceChar the choice character index to be parsed
     * @return the parsed choice character index or null
     */
    protected int getChoiceCharIndex(String choiceChar) {
        return choiceChar.toUpperCase().charAt(0) - 65;
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
     * Determine if the possible match contains valid a choices that have not
     * yet been chosen.
     *
     * @param possibleMatch the possible match
     * @return true if neither of the choices in the possible match have been
     * chosen yet, otherwise false
     */
    protected boolean isValidMatch(String possibleMatch) {
        int i;
        boolean isValidMatch;
        String[] existingMatch;

        // Split the possible match.
        String[] matchArray = splitMatch(possibleMatch);

        if (isValidMatch = (matchArray != null)) {
            // Loop through existing user matches.
            for (String s : questionResponse.getResponseList()) {
                // Split the existing user match.
                existingMatch = splitMatch(s);

                // Test if either choice from the new match has already been recorded.
                for (i = 0; i < matchArray.length; i++)
                    if (!(isValidMatch = !matchArray[i].equals(existingMatch[i]))) break;
            }
        }

        return isValidMatch;
    }
}
