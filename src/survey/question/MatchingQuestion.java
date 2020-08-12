package survey.question;

import survey.SurveyApp;

import java.util.ArrayList;
import java.util.List;

public class MatchingQuestion extends Question {
    protected List<ChoiceList> choiceSet;

    public MatchingQuestion() {
        super();
        choiceSet = new ArrayList<>() {
            {
                add(new ChoiceList());
                add(new ChoiceList());
            }
        };
    }

    @Override
    public String getQuestionType() {
        return "Matching";
    }

    public List<ChoiceList> getChoiceSet() {
        return choiceSet;
    }

    @Override
    public void create() {
        int i, j;
        int choiceSetSize = choiceSet.size();
        int choiceNum = 1;
        char choiceChar = 'A';
        String choice;

        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of matches.
        numResponses = getValidNumMatches();

        // Get valid matching choices.
        for (i = 0; i < choiceSetSize; i++) {
            for (j = 0; j < numResponses; j++) {

                // Display question choice prompt.
                if (i % 2 == 0) {
                    SurveyApp.out.displayMenuPrompt("Enter choice " + choiceChar + ").");
                    choiceChar++;
                } else {
                    SurveyApp.out.displayMenuPrompt("Enter choice " + choiceNum + ").");
                    choiceNum++;
                }

                // Get valid choice.
                choice = getValidChoice();

                // Add choice to choice list.
                choiceSet.get(i).add(choice);
            }
        }
    }

    protected int getValidNumMatches() {
        Integer numMatches;
        boolean isValidNumMatches;

        // Get question type.
        String questionType = getQuestionType();

        do {
            // Record number of question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of matches for your " + questionType + " question.");
            numMatches = SurveyApp.in.readQuestionChoiceCount();

            // Check if valid number of choices.
            if (!(isValidNumMatches = isValidNumMatches(numMatches))) {
                SurveyApp.displayInvalidInputMessage("number");
            }
        } while (!isValidNumMatches);

        return numMatches;
    }

    protected boolean isValidNumMatches(Integer i) {
        return i != null && i > 1;
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
}
