package survey;

import menu.CreateQuestionMenu;
import survey.question.Question;
import survey.response.SurveyResponse;
import utils.FileConfiguration;

import java.io.File;

public class Test extends Survey {
    private static long serialVersionUID = 1L;
    private static final String basePath = FileConfiguration.SERIALIZED_FILES_DIRECTORY + "Test" + File.separator;
    protected SurveyResponse answerKey;

    public Test() {
        surveyType = "Test";
        name = createSurveyName();
        answerKey = new SurveyResponse(name);
    }

    @Override
    public void create() {
        String choice;
        Question q;
        QuestionFactory qf = new QuestionFactory();

        // Loop until user quits.
        do {
            // Get user choice from question menu.
            choice = SurveyApp.getUserMenuChoice(CreateQuestionMenu.PROMPT, CreateQuestionMenu.OPTIONS);

            if (!choice.equals(CreateQuestionMenu.RETURN)) {
                // Use survey.question factory to get new question.
                q = qf.getQuestion(choice);

                try {
                    // Create survey.question specific attributes.
                    q.create();
                } catch (NullPointerException ignore) {
                    SurveyApp.out.displayNote("That type of question cannot be created right now. Please try another.");
                    q = null;
                }

                if (q != null) {
                    // Get the correct
                    // Add survey.question to question list.
                    questionList.add(q);

                    SurveyApp.out.displayNote("Successfully added new " + q.getQuestionType() + " question.");
                }
            }
        } while (!choice.equals(CreateQuestionMenu.RETURN));
    }
}
