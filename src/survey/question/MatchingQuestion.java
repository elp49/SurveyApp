package survey.question;

import menu.ModifyQuestionMenu;
import survey.SurveyApp;
import survey.response.QuestionResponse;
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
        char choiceChar;
        ChoiceList choiceList;
        List<ChoiceList> result = new ArrayList<>();

        for (i = 1; i <= numChoiceLists; i++) {
            SurveyApp.out.displayNote("Column #" + i);

            // Set choice character.
            if (i % 2 == 0) choiceChar = '1';
            else choiceChar = 'A';

            // Get valid choice list.
            choiceList = getValidChoiceList(choiceChar);

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
    protected ChoiceList getValidChoiceList(char choiceChar) {
        int i;
        String choice;
        ChoiceList result = new ChoiceList();

        for (i = 1; i <= numResponses; i++) {
            // Display survey.question choice prompt.
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
     * Read user input until a valid choice is provided.
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

            // If the choice is invalid,
            // then isValidChoice will be false.
        } while (!isValidChoiceText);

        return choice;
    }

    /**
     * Determine if the given choice is valid, null, or blank.
     *
     * @param choice the choice to be validated
     * @return true if the choice is not null or blank, otherwise false
     */
    protected boolean isValidChoiceText(String choice) {
        return !Validation.isNullOrBlank(choice);
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestion(prompt, choiceSet);
    }

    @Override
    public void modify(boolean isTest) {
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
     */
    protected void modifyChoiceSet() {
        // Determine if user will modify question choices.
        String menuChoice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_CHOICES, ModifyQuestionMenu.OPTIONS);

        // Test if the user chose to modify choice set.
        if (menuChoice.equals(ModifyQuestionMenu.YES)) {
            // Display choice set.
            SurveyApp.out.displayQuestion(new String[]{
                    "Which choice do you want to modify?",
                    "Enter the character or number."
            }, choiceSet);

            // Modify choice.
            modifyChoice();
        }
    }

    /**
     * Get the choice the user will modify and determine which choice list
     * it is in. If the user enters a possible choice, then get and set
     * the new choice.
     */
    protected void modifyChoice() {
        String response;
        boolean isPossibleChoice;

        // Loop until user enters a possible choice.
        do {
            // Get choice to be modified.
            response = SurveyApp.in.readQuestionResponse();

            // Test is user entered possible character.
            if (isPossibleChoice = isPossibleChar(response))
                modifyColumnOneChoice(response);

                // Test is user entered possible number.
            else if (isPossibleChoice = isPossibleNumber(response))
                modifyColumnTwoChoice(response);

                // Test is user entered impossible choice.
            else
                SurveyApp.out.displayNote(response + " is not a valid choice.");

            // If the user enters an impossible choice character or number,
            // then isPossibleChoice will be false.
        } while (!isPossibleChoice);
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
        return choiceChar.charAt(0) - 65;
    }

    /**
     * Get and set the new column one choice.
     *
     * @param choiceChar the choice character
     */
    protected void modifyColumnOneChoice(String choiceChar) {
        // Get index of choice character.
        int choiceIndex = getChoiceCharIndex(choiceChar);

        // Get the new choice to be set.
        SurveyApp.out.displayMenuPrompt("Enter the new choice " + choiceChar + ").");
        String newChoice = getValidChoiceText();

        // Set the new choice.
        setChoice(0, choiceIndex, newChoice);
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
            isPossibleNumber = Validation.isInRange(num, 0, choiceSet.get(1).size() - 1);

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
     * Get and set the new column two choice.
     *
     * @param choiceNum the choice number
     */
    protected void modifyColumnTwoChoice(String choiceNum) {
        // Get index of choice number.
        int choiceIndex = getChoiceNumIndex(choiceNum);

        // Get the new choice to be set.
        SurveyApp.out.displayMenuPrompt("Enter the new choice " + choiceNum + ").");
        String newChoice = getValidChoiceText();

        // Set the new choice.
        setChoice(1, choiceIndex, newChoice);
    }

    /**
     * Set the choice at the specified position.
     *
     * @param choiceListIndex the index of the choice list in the choice set
     * @param choiceIndex     the index of the choice in the choice list
     * @param choice          the new choice
     */
    protected void setChoice(int choiceListIndex, int choiceIndex, String choice) {
        choiceSet.get(choiceListIndex).set(choiceIndex, choice);
    }

    @Override
    public String readValidQuestionResponse() {
        boolean isValidMatch;
        String response;
        String[] possibleMatch;

        // Loop until user enters a possible match.
        do {
            // Get user's match.
            response = SurveyApp.in.readQuestionResponse().toUpperCase();

            // Test null or blank response.
            if (!(isValidMatch = !Validation.isNullOrBlank(response)))
                SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

            // Test response length less than 2.
            else if (!(isValidMatch = response.length() >= 2))
                SurveyApp.out.displayNote("Enter both choices on together (e.g. A1).");

            else {
                // Split the match.
                possibleMatch = splitMatch(response);

                // Test choice character index is out of range of choice list.
                if (!(isValidMatch = isPossibleChar(possibleMatch[0])))
                    SurveyApp.out.displayNote(possibleMatch[0] + " is not a available character.");

                // Test choice number index if out of range of choice list.
                else if (!(isValidMatch = isPossibleNumber(possibleMatch[1])))
                    SurveyApp.out.displayNote(possibleMatch[1] + " is not a available number.");

                // Test either choice has already been chosen.
                else
                    isValidMatch = isValidMatch(possibleMatch);
            }

            // If user enters an invalid response,
            // then isValidMatch will be false.
        } while (!isValidMatch);

        return response;
    }

    /**
     * Parse each column and separate the given match string into an array.
     *
     * @param match the match to be split
     * @return the split match
     */
    protected String[] splitMatch(String match) {
        return new String[]{
                match.substring(0, 1),
                match.substring(1)
        };
    }

    /**
     * Determine if the provided question response is valid match.
     *
     * @param match the user entered match
     * @return true if the question response is valid, otherwise false
     */
    protected boolean isValidMatch(String[] match) {
        int i;
        String[] existingMatch;
        boolean isValidResponse = true;

        if (questionResponse != null && !questionResponse.getResponseList().isEmpty()) {
            // Loop through existing user matches.
            for (String s : questionResponse.getResponseList()) {
                // Split the existing user match.
                existingMatch = splitMatch(s);

                // Test if either choice from the new match has already been recorded.
                for (i = 0; i < match.length; i++) {
                    if (!(isValidResponse = !match[i].equals(existingMatch[i]))) {
                        SurveyApp.out.displayNote("You've already entered choice " + match[i]);
                        break;
                    }
                }

                // If one of the choices have already been entered,
                // then isValidResponse will be false.
                if (!isValidResponse)
                    break;
            }
        }

        return isValidResponse;
    }

    @Override
    public void tabulate(List<QuestionResponse> questionResponseList) {
        int newCount, i;
        boolean isTheSameResponse = false;
        List<String> responseList, l;
        List<List<String>> resultResponseList = new ArrayList<>();
        List<Integer> resultResponseCountList = new ArrayList<>();

        // Create result lists.
        for (QuestionResponse qr : questionResponseList) {
            // Get responses list.
            responseList = qr.getResponseList();

            // Test is result response list is empty.
            if (!resultResponseList.isEmpty()) {
                for (i = 0; i < resultResponseList.size(); i++) {
                    // Get the result response.
                    l = resultResponseList.get(i);

                    for (String s : responseList)
                        if (!(isTheSameResponse = l.contains(s)))
                            break;

                    // If this list in the result response list contains all of the same
                    // responses as the response list, then isTheSameResponse will be true.
                    if (isTheSameResponse) {
                        // Get new response count.
                        newCount = resultResponseCountList.get(i) + 1;

                        // Increment response count.
                        resultResponseCountList.set(i, newCount);
                    } else {
                        // Add response to result response list.
                        resultResponseList.add(responseList);
                        resultResponseCountList.add(1);
                    }
                }
            } else {
                // Add response to result response list.
                resultResponseList.add(responseList);

                // Increment response count.
                resultResponseCountList.add(1);
            }
        }

        // Display question prompt.
        SurveyApp.out.displayQuestion(prompt);

        for (i = 0; i < resultResponseList.size(); i++) {
            SurveyApp.out.displayQuestionResponse(resultResponseCountList.get(i).toString());
            for (String s : resultResponseList.get(i))
                SurveyApp.out.displayQuestionResponse(s);

            // Line break.
            SurveyApp.out.displayNote("", true);
        }
    }

    @Override
    public QuestionResponse readCorrectAnswer() {
        int i;
        String correctAnswer;
        QuestionResponse answerKey = new QuestionResponse();

        // Display choices.
        SurveyApp.out.displayQuestionChoiceSet(choiceSet);

        for (i = 1; i <= numResponses; i++) {
            // Read a correct answer.
            SurveyApp.out.displayMenuPrompt("Enter correct " + responseType + " #" + i + ":");
            correctAnswer = readValidQuestionResponse();

            // Add correct answer to answer key.
            answerKey.add(correctAnswer);
        }

        return answerKey;
    }

    @Override
    public void displayAnswer(QuestionResponse answer) {
        int i;
        int choiceCharIndex, choiceNumIndex;
        String[] match;

        // Get choice lists.
        ChoiceList colOne = choiceSet.get(0);
        ChoiceList colTwo = choiceSet.get(1);

        SurveyApp.out.displayNote("The correct " + responseType + "es are: ");

        // Display correct matches.
        for (i = 0; i < answer.size(); i++) {
            // Split the match.
            match = splitMatch(answer.get(i));

            // Get choice indices.
            choiceCharIndex = getChoiceCharIndex(match[0]);
            choiceNumIndex = getChoiceNumIndex(match[1]);

            SurveyApp.out.displayQuestionResponse(
                    match[0] + ") " + colOne.get(choiceCharIndex) + ", " +
                            match[1] + ") " + colTwo.get(choiceNumIndex)
            );
        }
    }
}
