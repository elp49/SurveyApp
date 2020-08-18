package survey.question;

import survey.SurveyApp;
import utils.Validation;

public class ValidDateQuestion extends ShortAnswerQuestion {
    protected final String MONTH_FORMAT = "MM";
    protected final String DAY_FORMAT = "DD";
    protected final String YEAR_FORMAT = "YYYY";
    protected final String SEPARATOR = "-";
    protected final String DATE_FORMAT = MONTH_FORMAT + SEPARATOR + DAY_FORMAT + SEPARATOR + YEAR_FORMAT;

    public ValidDateQuestion() {
        super();
        questionType = "Valid Date";
        responseType = "date";
    }

    @Override
    public void create() {
        SurveyApp.out.displayNote("Dates will be recorded in the following format: " + DATE_FORMAT);

        // Get valid prompt.
        prompt = getValidPrompt();

        // Get valid number of responses.
        numResponses = getValidNumResponses();
    }

    @Override
    public void display() {
        SurveyApp.out.displayQuestionPrompt(new String[]{
                prompt,
                "A date should be entered in the following format: " + DATE_FORMAT,
                "Please give " + numResponses + " date(s)."
        });
    }

    /**
     * Determines if the date is valid. If it is invalid, report why.
     *
     * @return the response string
     */
    protected String getValidResponse() {
        // Record user response.
        String response = SurveyApp.in.readQuestionResponse();

        // Test for null or blank string.
        if (!Validation.isNullOrBlank(response))
            SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

            // Test for invalid date format or non integer values.
        else if (!isValidDate(response)) {
            SurveyApp.out.displayNote("Your " + responseType + " must be in the following format: " + DATE_FORMAT);

            // Clean up invalid response.
            response = null;
        }

        return response;
    }

    /**
     * Determine if the user entered date string is valid.
     *
     * @param date the user entered date string
     * @return true if it is valid, otherwise false
     */
    protected boolean isValidDate(String date) {
        int[] dateArray;

        // Get formatted date array or null if date does not follow format.
        if (null == (dateArray = getFormattedDateArray(date))) return false;

        return isRealDate(dateArray[0], dateArray[1], dateArray[2]);
    }

    /**
     * Build and return a formatted date array of type int[] of size three.
     * The elements are month, day, and year respectively.
     *
     * @param date the date string to be formatted
     * @return the formatted date string
     */
    protected int[] getFormattedDateArray(String date) {
        int i;
        String[] responseStrArray, formatStrArray;
        int[] dateArray = new int[3];

        // Test valid response length.
        if (date.length() == DATE_FORMAT.length()) return null;

        // Split response and date format into array.
        responseStrArray = date.split(SEPARATOR);
        formatStrArray = DATE_FORMAT.split(SEPARATOR);

        // Test invalid number of tokens.
        if (responseStrArray.length != formatStrArray.length) return null;

        for (i = 0; i < responseStrArray.length; i++) {
            // Test length of each element.
            if (responseStrArray[i].length() != formatStrArray[i].length()) return null;

            try {
                // Test invalid number.
                dateArray[i] = Integer.parseInt(responseStrArray[i]);
            } catch (NumberFormatException ignore) {
                return null;
            }
        }

        return dateArray;
    }

    /**
     * Determine if the provided date is a real date. Ignore years before 1 AD.
     *
     * @param month the month number
     * @param day   the day number
     * @param year  the year number
     * @return true if it is a real date, otherwise false
     */
    protected boolean isRealDate(int month, int day, int year) {
        // Test out of range month.
        if (month < 1 || month > 12) return false;

        // Test out of range day.
        if (day < 1 || day > 31) return false;

        // Test out of range year.
        if (year < 1) return false;

            // Test months with only 30 days.
        else if (day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) return false;

            // Test for leap year.
        else if (month == 2 && day == 29) {
            if (year % 4 != 0) return false;
            else if (year % 100 == 0 && year % 400 != 0) return false;
        }

        return true;
    }
}
