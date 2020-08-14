package survey.question;

import menu.ModifyQuestionMenu;
import survey.SurveyApp;

import java.util.ArrayList;
import java.util.List;

public class MatchingQuestion extends Question {
    protected final int numChoiceLists = 2;
    protected List<ChoiceList> choiceSet;

    public MatchingQuestion() {
        super();
        choiceSet = new ArrayList<>();
    }

    @Override
    public String getQuestionType() {
        return "Matching";
    }

    public List<ChoiceList> getChoiceSet() {
        return choiceSet;
    }

    // TODO: figure out if you like the assignment look
    //  or build() / create() function look better.
    @Override
    public void create() {
        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of matches.
        numResponses = getValidNumResponses("matches");

        // Build valid matching choice set.
        choiceSet = getValidChoiceSet();
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
        return !SurveyApp.isNullOrEmpty(s);
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
        int choiceListIndex, choiceIndex, i;
        String newChoice;
        boolean isReturn = false;
        List<String> choiceList;
        List<String> options = new ArrayList<>();

        // Determine if user will modify question choices.
        String choice = SurveyApp.getUserMenuChoice(ModifyQuestionMenu.MODIFY_CHOICES, ModifyQuestionMenu.OPTIONS);

        // Display choice set.
        SurveyApp.out.displayQuestionChoiceSet(choiceSet);

        switch (choice) {
            case ModifyQuestionMenu.YES:

                // Create list of possible columns to be modified.
                for (i = 1; i <= choiceSet.size(); i++)
                    options.add("Column #" + i);

                // Get choice list to be modified.
                choice = SurveyApp.getUserMenuChoice("Which column of choices do you want to modify?", options);

                // Get index of choice in options list.
                choiceListIndex = options.indexOf(choice);

                // Get choice to be modified from chosen choice list.
                choiceList = choiceSet.get(choiceListIndex).getChoices();
                choice = SurveyApp.getUserMenuChoice("Which choices do you want to modify in column #" + (choiceListIndex + 1) + "?", choiceList);

                // Get index of choice in choice list.
                choiceIndex = choiceList.indexOf(choice);

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
}
