package survey.question;

public class DateQuestion extends ShortAnswerQuestion {
    protected static final String dateFormat = "MM-DD-YYYY";
    protected static final int responseCharLimit = dateFormat.length();

    public DateQuestion() {
        super();
    }

    @Override
    public String getQuestionType() {
        return "Date";
    }

    public static String getDateFormat() {
        return dateFormat;
    }
}
