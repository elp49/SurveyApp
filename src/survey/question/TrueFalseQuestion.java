package survey.question;

import survey.SurveyApp;
import survey.response.QuestionResponse;

import java.util.ArrayList;
import java.util.List;

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
        SurveyApp.out.displayQuestion(prompt, choiceList);
    }

    @Override
    public void modify() {
        // Modify the question prompt.
        modifyPrompt();
    }

    @Override
    public void tabulate(List<QuestionResponse> questionResponseList) {
        int responseIndex, newCount, i;
        String TRUE = "A";
        List<String> responseList;
        List<Integer> resultResponseCountList = new ArrayList<>();

        // Create result response count list.
        for (i = 0; i < choiceList.size(); i++)
            resultResponseCountList.add(0);

        // Increment result response count list.
        for (QuestionResponse qr : questionResponseList) {
            // Get responses list.
            responseList = qr.getResponseList();

            // Add all responses to result response list.
            for (String s : responseList) {
                // Get index of response in result response list.
                if (s.equals(TRUE))
                    responseIndex = 0;
                else
                    responseIndex = 1;

                // Get new response count.
                newCount = resultResponseCountList.get(responseIndex) + 1;

                // Increment response count.
                resultResponseCountList.set(responseIndex, newCount);
            }
        }

        // Display question prompt.
        SurveyApp.out.displayQuestion(prompt);

        for (i = 0; i < choiceList.size(); i++) {
            SurveyApp.out.displayQuestionResponse(choiceList.get(i) + ": " + resultResponseCountList.get(i));
        }
    }
}
