package survey.question;

public class DateQuestion extends ShortAnswerQuestion {
    protected static final String dateFormat = "MM-DD-YYYY";

    public DateQuestion() { }

    public static String getDateFormat() {
        return dateFormat;
    }
}
