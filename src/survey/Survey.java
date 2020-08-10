package survey;

import survey.question.Question;
import survey.question.QuestionFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Survey implements Serializable {
    private static long serialVersionUID = 1L;
    private static String basePath = "Surveys";
    private String surveyName;
    private QuestionFactory questionFactory;
    private List<Question> questionList;
    /*private SurveyResponse response;*/

    public Survey(QuestionFactory questionFactory) {
        this.questionFactory = questionFactory;
        questionList = new ArrayList<>();
    }

    public String getSurveyName() {
        return surveyName;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void create() {
        String choiceStr;
        Question q;

        // Loop until user quits.
        do {
            // Display main menu.
            SurveyApp.out.displayMenu(QuestionFactory.QuestionMenu.PROMPT, QuestionFactory.QuestionMenu.OPTIONS);

            // Get user menu choice.
            choiceStr = SurveyApp.in.readValidMenuChoice(QuestionFactory.QuestionMenu.OPTIONS, -1);

            if (!SurveyApp.isNullOrEmpty(choiceStr)) {
                if (!choiceStr.equals(QuestionFactory.QuestionMenu.RETURN)) {
                    // Use question factory to get new question.
                    q = questionFactory.getQuestion(choiceStr);

                    try {
                        // Create question specific attributes.
                        q.create();
                    } catch (NullPointerException ignore) {
                        continue;
                    }

                    // Add question to question list.
                    questionList.add(q);
                }
            } else {
                SurveyApp.displayInvalidInputMessage("choice");
            }
        } while (!choiceStr.equals(QuestionFactory.QuestionMenu.RETURN));
    }

    public void addQuestion() {
    }

    public void display() {
    }

    public static Survey load() {
        return null;
    }

    public void save() {
    }

    public void take() {
    }

    public void modify() {
    }

    public static Survey deserialize() {
        return null;
    }
}
