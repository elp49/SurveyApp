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

    /*@Override
    public List<String> getValidResponseList() {
        int i;
        String response, reason;
        boolean isNullOrEmpty;
        List<String> responseList = new ArrayList<>();

        // Loop until user gives valid response(s).
        for (i = 1; i <= numResponses; i++) {
            do {
                // Record response.
                response = SurveyApp.in.readQuestionResponse();

                // Get reason why response is invalid or empty string if is valid.
                reason = getReasonWhyResponseIsInvalid(response);

                // Test if valid response.
                if (!(isNullOrEmpty = SurveyApp.isNullOrEmpty(reason))) {
                    SurveyApp.displayInvalidInputMessage(getResponseType());
                    SurveyApp.out.displayNote(reason);
                }

                // If the user response is invalid, getReasonWhyResponseIsInvalid will
                // return the reason why as a string and isNullOrEmpty will be false.
            } while (!isNullOrEmpty);

            // Add response to response list.
            responseList.add(response);
        }

        return responseList;
    }*/


    /**
     * Determines if the question response is a valid date.
     *
     * @param response the valid date.
     * @return true if the response is valid, otherwise false.
     */
    @Override
    protected boolean performResponseValidation(String response) {
        boolean isValid;

        // Test for null or blank string.
        if (!(isValid = !Validation.isNullOrBlank(response)))
            SurveyApp.out.displayNote("Your " + responseType + " cannot be empty.");

            // Test for incorrect date format or non integer values.
        else if (!(isValid = isValidDate(response)))
            SurveyApp.out.displayNote("Your " + responseType + " must be entered in the following format: "
                    + DATE_FORMAT);

        return isValid;
    }

    //TODO: give more description response for why invalid.
    protected boolean isValidDate(String date) {
        int i;
        String[] responseStrArray, formatStrArray;
        int[] responseIntArray = new int[3];

        // Test invalid response length.
        if (date.length() != DATE_FORMAT.length()) return false;

        // Split response and date format into array.
        responseStrArray = date.split("-");
        formatStrArray = DATE_FORMAT.split("-");

        // Test invalid number of tokens.
        if (responseStrArray.length != formatStrArray.length) return false;

        for (i = 0; i < responseStrArray.length; i++) {
            // Test length of each element.
            if (responseStrArray[i].length() != formatStrArray[i].length()) return false;

            try {
                // Test invalid number.
                responseIntArray[i] = Integer.parseInt(responseStrArray[i]);
            } catch (NumberFormatException ignore) {
                return false;
            }
        }

        // Test out of range month.
        if (responseIntArray[0] < 1 || responseIntArray[0] > 12) return false;

        // Test out of range day.
        if (responseIntArray[1] < 1 || responseIntArray[1] > 31) return false;

        // Test out of range year.
        if (responseIntArray[2] < 1) return false;

        //TODO: validate months with less than 31 days.

        return true;
    }
}
