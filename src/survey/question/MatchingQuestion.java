package survey.question;

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
        SurveyApp.out.displayQuestionChoiceSet(choiceSet);
    }
}
