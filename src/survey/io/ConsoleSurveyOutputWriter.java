package survey.io;

import survey.question.ChoiceList;
import utils.ConsoleOutputWriter;
import utils.OutputWriter;

import java.util.List;

public class ConsoleSurveyOutputWriter implements SurveyOutputWriter {
    private OutputWriter out;

    public ConsoleSurveyOutputWriter() {
        out = new ConsoleOutputWriter();
    }

    private void print(String s) {
        out.print(s);
    }

    private void println(String s) {
        out.println(s);
    }

    private String getLineSeparator() {
        return out.getLineSeparator();
    }

    public void displayNote(String note) {
        println(getLineSeparator() + note);
    }

    public void displayMenu(String prompt, List<String> choices) {
        displayMenuPrompt(prompt);
        displayMenuOptions(choices);
    }

    public void displayMenuPrompt(String prompt) {
        println(getLineSeparator() + prompt);
    }

    public void displayMenuOptions(List<String> options) {
        int i;
        String line;

        // Check if inline.
        for (i = 1; i <= options.size(); i++) {
            line = i + ") " + options.get(i - 1);
            println(line);
        }
    }

    //TODO: decide if should remove this method and dynamically create a menu with survey names as options.
    // and then use abody display menu function instead.
    public void displayAllSurveyNames(List<String> names) {
        int i;
        String line;

        for (i = 1; i <= names.size(); i++) {
            line = i + ") " + names.get(i - 1);
            println(line);
        }
    }

    public void displaySurvey(String survey) {
        println(survey);
    }

    public void displayQuestionPrompt(String prompt) {
        println(prompt);
    }

    public void displayQuestionChoiceList(ChoiceList choiceList) {
        int i = 0;
        char choiceChar = 'A';
        StringBuilder sb = new StringBuilder();

        for (String c : choiceList.getChoices()) {
            // Print a new line after every four choices.
            if (i % 4 == 0) {
                println(sb.toString());
                sb = new StringBuilder();
            }

            // Print choice string.
            sb.append(choiceChar).append(") ").append(c).append("\t");

            choiceChar++;
            i++;
        }

        // Print any leftover choices.
        if (sb.length() > 0) println(sb.toString());
    }

    public void displayQuestionChoiceSet(List<ChoiceList> choiceSet) {
        int i;
        char choiceChar = 'A';
        int choiceNum = 1;
        int size = getLargestChoiceListSize(choiceSet);
        StringBuilder sb = new StringBuilder();

        for (i = 0; i < size; i++) {
            for (ChoiceList cl : choiceSet) {
                // Append choice indent mark.
                if (i % 2 == 0) sb.append(choiceChar);
                else sb.append(choiceNum);

                // Append choice.
                sb.append(") ").append(cl.get(i)).append("\t");
            }

            println(sb.toString());
            sb = new StringBuilder();
        }
    }

    private int getLargestChoiceListSize(List<ChoiceList> choiceSet) {
        int size;
        int max = 0;

        for (ChoiceList cl : choiceSet) {
            size = cl.size();
            if (size > max) max = size;
        }

        return max;
    }

    public void displayQuestionResponse(String response) {
        println(response);
    }

    public void displaySurveyResponse(String response) {
        println(response);
    }
}
