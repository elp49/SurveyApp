package survey;

import menu.CreateQuestionMenu;
import survey.question.*;

import java.io.Serializable;

public class QuestionFactory implements Serializable {
    private static long serialVersionUID = 1L;

    public QuestionFactory() {
    }

    public Question getQuestion(String questionType) {
        Question q = null;

        switch (questionType) {
            case CreateQuestionMenu.TRUE_FALSE:
                q = new TrueFalseQuestion();
                break;
            case CreateQuestionMenu.MULTI_CHOICE:
                q = new MultipleChoiceQuestion();
                break;
            case CreateQuestionMenu.SHORT_ANSWER:
                q = new ShortAnswerQuestion();
                break;
            case CreateQuestionMenu.ESSAY:
                q = new EssayQuestion();
                break;
            case CreateQuestionMenu.DATE:
                q = new DateQuestion();
                break;
            case CreateQuestionMenu.MATCHING:
                q = new MatchingQuestion();
                break;
        }

        return q;
    }
}
