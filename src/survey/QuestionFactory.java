package survey;

import survey.question.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionFactory implements Serializable {
    private static long serialVersionUID = 1L;

    public QuestionFactory() {
    }

    public Question getQuestion(String questionType) {
        Question q = null;

        switch (questionType) {
            case QuestionMenu.TRUE_FALSE:
                q = new TrueFalseQuestion();
                break;
            case QuestionMenu.MULTI_CHOICE:
                q = new MultipleChoiceQuestion();
                break;
            case QuestionMenu.SHORT_ANSWER:
                q = new ShortAnswerQuestion();
                break;
            case QuestionMenu.ESSAY:
                q = new EssayQuestion();
                break;
            case QuestionMenu.DATE:
                q = new DateQuestion();
                break;
            case QuestionMenu.MATCHING:
                q = new MatchingQuestion();
                break;
        }

        return q;
    }

    public static class QuestionMenu {
        public static final String PROMPT = "What type of survey.question would you like to add?";
        public static final String TRUE_FALSE = "Add a new T/F survey.question";
        public static final String MULTI_CHOICE = "Add a new multiple-choice survey.question";
        public static final String SHORT_ANSWER = "Add a new short answer survey.question";
        public static final String ESSAY = "Add a new essay survey.question";
        public static final String DATE = "Add a new date survey.question";
        public static final String MATCHING = "Add a new matching survey.question";
        public static final String RETURN = "Return to previous menu";
        public static final List<String> OPTIONS = new ArrayList<>() {
            {
                add(TRUE_FALSE);
                add(MULTI_CHOICE);
                add(SHORT_ANSWER);
                add(ESSAY);
                add(DATE);
                add(MATCHING);
                add(RETURN);
            }
        };
    }
}
