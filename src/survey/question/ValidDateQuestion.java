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
        SurveyApp.out.displayQuestion(new String[]{
                prompt,
                "A date should be entered in the following format: " + DATE_FORMAT,
                "Please give " + numResponses + " date(s)."
        });
    }

    @Override
    protected String readPossibleQuestionResponse() {
        boolean isPossibleResponse;
        String response;

        do {
            // Get question response.
            response = SurveyApp.in.readQuestionResponse();

            // Test for null or blank string.
            if (!(isPossibleResponse = !Validation.isNullOrBlank(response)))
                SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

                // Test if response follows date format.
            else if (!(isPossibleResponse = (getFormattedDateArray(response) != null)))
                SurveyApp.out.displayNote("Your " + responseType + " must be in the following format: " + DATE_FORMAT);

            // If the user enters an impossible question response,
            // then isPossibleResponse will be false.
        } while (!isPossibleResponse);

        return response;
    }

    @Override
    protected boolean isValidResponse(String response) {
        // Get formatted date array.
        int[] dateArray = getFormattedDateArray(response);

        // Determine if the response contains a real date.
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
            SurveyApp.out.displayNote("Month " + month + " is out of range. The month should be be within 1 - 12");

            // Test out of range day.
        else if (!(isRealDate = (day >= 1 && day <= 31)))
            SurveyApp.out.displayNote("Day " + day + " is out of range. The day should be be within 1 - 31");

            // Test out of range year.
        else if (!(isRealDate = (year >= 1)))
            SurveyApp.out.displayNote("Year " + year + " is out of range. The year should be be within 1 - 9999");

            // Test months with only 30 days.
        else if (!(isRealDate = !(day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11))))
            SurveyApp.out.displayNote("Month " + month + " doesn't have 31 days.");

            // Test for leap year.
        else if (month == 2 && day == 29) {
            if (!(isRealDate = !((year % 4 != 0) || (year % 100 == 0 && year % 400 != 0))))
                SurveyApp.out.displayNote(year + " is not a leap year. February does not have 29 days in " + year + ".");
        }

        return isRealDate;
    }
}
