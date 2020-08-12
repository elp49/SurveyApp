package survey.question;

import survey.SurveyApp;

public class MultipleChoiceQuestion extends Question {
    protected ChoiceList choiceList;

    public MultipleChoiceQuestion() {
        super();
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
        int numChoices, i;
        String choice;

        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of choices.
        numChoices = getValidNumChoices();

        // Get valid choices.
        for (i = 0; i < numChoices; i++) {
            // Add choice to choice list.
            choice = getValidChoice();
            choiceList.add(choice);
        }

        // Get valid number of responses.
        numResponses = getValidNumResponses(numChoices);
    }

    protected String getValidChoice() {
        int choiceNum;
        String choice;
        boolean isValidChoice;

        do {
            choiceNum = choiceList.size() + 1;

            // Record choice.
            SurveyApp.out.displayMenuPrompt("Enter choice #" + choiceNum + ".");
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

        // Get question type.
        String questionType = getQuestionType();

        do {
            // Record number of question responses.
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
        return i != null && i > 1;
    }

    protected int getValidNumResponses(int numChoices) {
        Integer numResponses;
        boolean isValidNumResponses;

        // Get question type.
        String questionType = getQuestionType();

        do {
            // Record number of question responses.
            SurveyApp.out.displayMenuPrompt("Enter the number of allowed responses for your " + questionType + " question.");
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
}
