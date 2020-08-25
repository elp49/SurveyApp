package survey.question;

import survey.SurveyApp;
import utils.Validation;

public class ValidDateQuestion extends ShortAnswerQuestion {
    protected final static String MONTH_FORMAT = "MM";
    protected final static String DAY_FORMAT = "DD";
    protected final static String YEAR_FORMAT = "YYYY";
    protected final static String SEPARATOR = "-";
    protected final static String DATE_FORMAT = MONTH_FORMAT + SEPARATOR + DAY_FORMAT + SEPARATOR + YEAR_FORMAT;
    protected final static int MONTH_MIN = 1;
    protected final static int MONTH_MAX = 12;
    protected final static int DAY_MIN = 1;
    protected final static int DAY_MAX = 31;
    protected final static int YEAR_MIN = 1;
    protected final static int YEAR_MAX = 9999;

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
        SurveyApp.out.displayQuestion(new String[]{
                prompt,
                "A date should be entered in the following format: " + DATE_FORMAT,
                "Please give " + numResponses + " date(s)."
        });
    }

    @Override
    protected String readValidQuestionResponse() {
        boolean isValidResponse;
        String response;
        int[] dateArray;

        do {
            // Get question response.
            response = SurveyApp.in.readQuestionResponse();

            // Test null or blank response.
            if (!(isValidResponse = !Validation.isNullOrBlank(response)))
                SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

            else {
                // Get formatted date array.
                dateArray = getFormattedDateArray(response);

                // Test invalid date format response.
                if (!(isValidResponse = (dateArray != null)))
                    SurveyApp.out.displayNote("Your " + responseType + " must be in the following format: " + DATE_FORMAT);

                // Test response is not real date.
                else
                    isValidResponse = isRealDate(dateArray[0], dateArray[1], dateArray[2]);
            }

            // If the user enters an invalid question response,
            // then isValidResponse will be false.
        } while (!isValidResponse);

        return response;
    }

    /**
     * Build and return a formatted date array of type int[] of size three.
     * The elements are month, day, and year respectively.
     *
     * @param date a date string to be formatted
     * @return a formatted date string or null if doesn't follow format
     */
    protected int[] getFormattedDateArray(String date) {
        int numTokens, i;
        String[] responseStrArray, formatStrArray;
        int[] dateArray;

        // Test response length not equal to date format length.
        if (date.length() != DATE_FORMAT.length())
            return null;

        // Split response and date format into array.
        responseStrArray = date.split(SEPARATOR);
        formatStrArray = DATE_FORMAT.split(SEPARATOR);

        // Test number of tokens not equal to date format number of tokens.
        if ((numTokens = responseStrArray.length) != formatStrArray.length)
            return null;

        // Initialize date array.
        dateArray = new int[numTokens];

        for (i = 0; i < numTokens; i++) {
            // Test length of element not equal to length of date format element.
            if (responseStrArray[i].length() != formatStrArray[i].length())
                return null;

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
     * Determine if the provided date is a real date.
     * Valid years are between 0001 and 9999.
     *
     * @param month the month number
     * @param day   the day number
     * @param year  the year number
     * @return true if it is a real date, otherwise false
     */
    protected boolean isRealDate(int month, int day, int year) {
        boolean isRealDate;

        // Test out of range month.
        if (!(isRealDate = (month >= 1 && month <= 12)))
            SurveyApp.out.displayNote("Month " + month + " is out of range. The month should be be within "
                    + MONTH_MIN + " - " + MONTH_MAX + ".");

            // Test out of range day.
        else if (!(isRealDate = (day >= 1 && day <= 31)))
            SurveyApp.out.displayNote("Day " + day + " is out of range. The day should be be within "
                    + DAY_MIN + " - " + DAY_MAX + ".");

            // Test out of range year.
        else if (!(isRealDate = (year >= 1)))
            SurveyApp.out.displayNote("Year " + year + " is out of range. The year should be be within "
                    + YEAR_MIN + " - " + YEAR_MAX + ".");

            // Test months with only 30 days.
        else if (!(isRealDate = !(day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11))))
            SurveyApp.out.displayNote("Month " + month + " doesn't have " + day + " days.");

            // Test for 30 days in February.
        else if (!(isRealDate = !(month == 2 && day == 30))) {
            SurveyApp.out.displayNote("February does not have " + day + " days.");
        }

        // Test for leap year.
        else if (month == 2 && day == 29) {
            if (!(isRealDate = !((year % 4 != 0) || (year % 100 == 0 && year % 400 != 0))))
                SurveyApp.out.displayNote(year + " is not a leap year. February does not have " + day + " days in "
                        + year + ".");
        }

        return isRealDate;
    }
}
