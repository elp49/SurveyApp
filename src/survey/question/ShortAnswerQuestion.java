package survey.question;

public class ShortAnswerQuestion extends EssayQuestion {
    protected static final int responseCharLimit = 64;

    public ShortAnswerQuestion() {
        super();
    }

    @Override
    public String getQuestionType() {
        return "Short Answer";
    }

    public int getResponseCharLimit() {
        return responseCharLimit;
    }
}
