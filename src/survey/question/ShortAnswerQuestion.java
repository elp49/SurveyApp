package survey.question;

public class ShortAnswerQuestion extends EssayQuestion {
    protected static final int responseCharLimit = 64;

    public ShortAnswerQuestion() { }

    @Override
    public String getQuestionType() {
        return "Short Answer";
    }

    public int getResponseSizeLimit() {
        return responseCharLimit;
    }
}
