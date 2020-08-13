package survey.question;

import survey.SurveyApp;

public class MultipleChoiceQuestion extends Question {
    protected int numChoices;
    protected ChoiceList choiceList;

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

    protected int getValidNumChoices() {
        Integer numChoices;
        boolean isValidNumChoices;

        // Get survey.question type.
        String questionType = getQuestionType();

        do {
            // Record number of survey.question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of choices for your " + questionType + " survey.question.");
            numChoices = SurveyApp.in.readQuestionChoiceCount();

            // Check if valid number of choices.
            if (!(isValidNumChoices = isValidNumChoices(numChoices))) {
                SurveyApp.displayInvalidInputMessage("number");
            }
        } while (!isValidNumChoices);

        return numChoices;
    }

    protected boolean isValidNumChoices(Integer i) {
        return i != null && i > 1;
    }

    protected int getValidNumResponses(int numChoices) {
        Integer numResponses;
        boolean isValidNumResponses;

        // Get survey.question type.
        String questionType = getQuestionType();

        do {
            // Record number of survey.question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of allowed responses for your " + questionType + " survey.question.");
            numResponses = SurveyApp.in.readQuestionChoiceCount();

            // Check if valid number of responses.
            if (!(isValidNumResponses = isValidNumResponses(numResponses, numChoices))) {
                SurveyApp.displayInvalidInputMessage("number");
            }
        } while (!isValidNumResponses);

        return numResponses;
    }

    protected boolean isValidNumResponses(Integer i, Integer numChoices) {
        return i != null && i > 0 && i <= numChoices;
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayNote("Please give " + numResponses + " choices.");
        SurveyApp.out.displayQuestionChoiceList(choiceList);
    }
}
