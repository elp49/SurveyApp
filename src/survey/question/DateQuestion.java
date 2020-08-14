package survey.question;

import survey.SurveyApp;

public class DateQuestion extends ShortAnswerQuestion {
    protected final String dateFormat = "MM-DD-YYYY";
    protected final int responseCharLimit = dateFormat.length();

    public DateQuestion() {
        super();
    }

    @Override
    public String getQuestionType() {
        return "Date";
    }

    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public void create() {
        SurveyApp.out.displayNote("Dates will be recorded in the following format: " + dateFormat);

        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of responses.
        numResponses = getValidNumResponses("answers");
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestionPrompt(prompt);
        SurveyApp.out.displayNote(new String[]{
                "A date should be entered in the following format: " + dateFormat,
                "Please give " + numResponses + " date(s)."
        }, true);
    }
}
