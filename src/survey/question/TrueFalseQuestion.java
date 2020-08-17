package survey.question;

import survey.SurveyApp;

import java.util.ArrayList;

public class TrueFalseQuestion extends MultipleChoiceQuestion {

    public TrueFalseQuestion() {
        prompt = "";
        numResponses = 1;
        questionType = "True/False";
        responseType = "choice";
        numChoices = 2;
        choiceList = new ChoiceList(new ArrayList<>() {
            {
                add("True");
                add("False");
            }
        });
    }

    @Override
    public void create() {
        // Get valid prompt.
        prompt = getValidPrompt();
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayQuestionChoiceList(choiceList, true);
    }

    @Override
    public void modify() {
        // Modify the question prompt.
        modifyPrompt();
    }

    /*@Override
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
    }*/

    /*@Override
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
    }*/
}
