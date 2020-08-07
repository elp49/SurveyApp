package survey.question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionFactory implements Serializable {
    private static long serialVersionUID = 1L;

    public QuestionFactory() { }

    public Question getQuestion(String questionType) {
        Question q = null;

        switch (questionType) {
            case QuestionMenu.TRUE_FALSE:
                q = null;
                break;
            case QuestionMenu.MULTI_CHOICE:
                q = null;
                break;
            case QuestionMenu.SHORT_ANSWER:
                q = null;
                break;
            case QuestionMenu.ESSAY:
                q = new EssayQuestion();
                break;
            case QuestionMenu.DATE:
                q = null;
                break;
            case QuestionMenu.MATCHING:
                q = null;
                break;
        }

        return q;
    }

    public static class QuestionMenu {
        public static final String PROMPT = "What type of question would you like to add?";
        public static final String TRUE_FALSE = "Add a new T/F question";
        public static final String MULTI_CHOICE = "Add a new multiple-choice question";
        public static final String SHORT_ANSWER = "Add a new short answer question";
        public static final String ESSAY = "Add a new essay question";
        public static final String DATE = "Add a new date question";
        public static final String MATCHING = "Add a new matching question";
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
