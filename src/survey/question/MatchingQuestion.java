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

    @Override
    protected boolean performResponseValidation(String response) {
        return false;
    }

    protected boolean modifyChoices() {
        int choiceListIndex, choiceIndex;
        String newChoice;
        boolean isReturn = false;

        // Determine if user will modify question choices.
        String choice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_CHOICES, ModifyQuestionMenu.OPTIONS);

        // Display choice set.
        SurveyApp.out.displayQuestionChoiceSet(choiceSet);

        switch (choice) {
            case ModifyQuestionMenu.YES:
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

    public List<String> getValidResponseList() {
        int i;
        boolean isValidMatch = true;
        String[] match;
        List<String> responseList = new ArrayList<>();

        for (i = 0; i < numResponses; i++) {
            do {
                // Get possible valid match as array of two strings.
                match = getPossibleValidMatch();

                // Test if either choice has already been recorded.
                for (String s : responseList)
                    if (!(isValidMatch = isValidMatch(match, s))) break;

                // If user enters a choice that has already been entered, then invalidMatch
                // will be true and loop will continue until a valid match is entered.
            } while (!isValidMatch);

            // Create response string and add to response list.
            responseList.add(match[0] + SEPARATOR + match[1]);
        }

        return responseList;
    }

    /**
     * Get a match that is possible valid. This will confirm that the two choices
     * in the match do exist within the possible combination of matches. However,
     * it does not check if either of those two choices in the match have already
     * been chosen.
     *
     * @return the possible valid match as a string array of size two
     */
    protected String[] getPossibleValidMatch() {
        String response;
        boolean isValid;
        String[] match = new String[2];

        do {
            // Get user matching response.
            response = SurveyApp.in.readQuestionResponse();

            if (isValid = !Validation.isNullOrBlank(response)) {
                // Parse first column character.
                match[0] = parseMatchChar(response);

                // Test first column choice.
                if (isValid = match[0].matches("([a-z]|([A-Z]))")) {
                    // Parse second column number.
                    match[1] = parseMatchNum(response);

                    // Test second column choice.
                    isValid = isValidNumber(match[1].trim());
                } else {
                    SurveyApp.displayInvalidInputMessage("response");
                }
            } else {
                SurveyApp.displayInvalidInputMessage("response");
            }
        } while (!isValid);

        return match;
    }

    private boolean isValidNumber(String num) {
        Integer n = null;
        boolean isValid = true;

        try {
            n = Integer.parseInt(num);
        } catch (NumberFormatException ignore) {
            isValid = false;
        }

        if (n != null) isValid = Validation.isInRange(n, 1, 26);

        return isValid;
    }

    /**
     * Determine is the provided match contains one of the same choices that is
     * in the pre-recorded match.
     *
     * @param match            the match
     * @param prerecordedMatch the pre-recorded match
     * @return true if the provided match does not contain any of the same
     * choices that are within the pre-recorded match, otherwise false
     */
    protected boolean isValidMatch(String[] match, String prerecordedMatch) {
        int i;
        String choiceChar;
        boolean isValidMatch = false;

        for (i = 0; i < match.length; i++) {
            // Parse pre-recorded choice.
            choiceChar = prerecordedMatch.split(":")[i];

            // Test each choice in match against pre-recorded match.
            if (!(isValidMatch = !match[i].equals(choiceChar))) {
                SurveyApp.out.displayNote("You already entered choice " + match[i]);
                break;
            }
        }

        return isValidMatch;
    }

    /**
     * Parse the first column character in match pair.
     *
     * @param matchPair the match pair that is to be parsed
     * @return the parsed first column character
     */
    protected String parseMatchChar(String matchPair) {
        return Character.toString(matchPair.charAt(0));
    }

    /**
     * Parse the second column number in match pair.
     *
     * @param matchPair the match pair that is to be parsed
     * @return the parsed second column number
     */
    protected String parseMatchNum(String matchPair) {
        return matchPair.substring(1).trim();
    }
}
