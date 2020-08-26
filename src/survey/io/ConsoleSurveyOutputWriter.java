package survey.io;

import survey.question.ChoiceList;
import utils.ConsoleOutputWriter;
import utils.OutputWriter;

import java.util.List;

public class ConsoleSurveyOutputWriter implements SurveyOutputWriter {
    private final OutputWriter out;

    public ConsoleSurveyOutputWriter() {
        out = new ConsoleOutputWriter();
    }

    private void print(String s) {
        out.print(s);
    }

    private void println(String s) {
        out.println(s);
    }

    private String lineSeparator() {
        return out.lineSeparator();
    }

    public void displayNote(String note) {
        displayNote(note, false);
    }

    public void displayNote(String note, boolean isInline) {
        String line = "";

        // Test if is inline note.
        if (!isInline) line += lineSeparator();

        // Append note.
        line += note;

        println(line);
    }


    // TODO:
    /*public void displayNote(String note) {
        displayNote(note, true);
    }

    public void displayNote(String note, boolean lineSepBefore) { displayNote(note, lineSepBefore, true); }

    public void displayNote(String note, boolean lineSepBefore, boolean lineSepAfter) {
        String line = "";

        // Test line separator before note.
        if (lineSepBefore)
            line += lineSeparator();

        // Append note.
        line += note;

        // Test line separator after note.
        if (lineSepAfter)
            line += lineSeparator();

        println(line);
    }.*/


    public void displayAllNotes(String[] note) {
        displayAllNotes(note, false);
    }

    public void displayAllNotes(String[] note, boolean isInline) {
        StringBuilder sb = new StringBuilder();
        String ls = lineSeparator();

        // Test if is inline notes.
        if (!isInline) sb.append(ls);

        // Append notes.
        for (String s : note)
            sb.append(s).append(ls);

        // Print notes.
        print(sb.toString());
    }

    public void displayMenu(String prompt, List<String> choices) {
        displayMenuPrompt(prompt);
        displayMenuOptions(choices);
    }

    public void displayMenu(String[] prompt, List<String> choices) {
        displayMenuPrompt(prompt);
        displayMenuOptions(choices);
    }

    public void displayMenuPrompt(String prompt) {
        println(lineSeparator() + prompt);
    }

    public void displayMenuPrompt(String[] prompt) {
        String ls = lineSeparator();
        StringBuilder sb = new StringBuilder();

        // Append each line in prompt.
        for (String s : prompt)
            sb.append(ls).append(s);

        // Print prompt.
        println(sb.toString());
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

    public void displayQuestion(String prompt) {
        displayQuestionPrompt(prompt);
    }

    public void displayQuestion(String[] prompt) {
        displayQuestionPrompt(prompt);
    }

    public void displayQuestion(String prompt, ChoiceList choiceList) {
        displayQuestionPrompt(prompt);
        displayQuestionChoiceList(choiceList);
    }

    public void displayQuestion(String[] prompt, ChoiceList choiceList) {
        displayQuestionPrompt(prompt);
        displayQuestionChoiceList(choiceList);
    }

    public void displayQuestion(String prompt, List<ChoiceList> choiceSet) {
        displayQuestionPrompt(prompt);
        displayQuestionChoiceSet(choiceSet);
    }

    public void displayQuestion(String[] prompt, List<ChoiceList> choiceSet) {
        displayQuestionPrompt(prompt);
        displayQuestionChoiceSet(choiceSet);
    }

    public void displayQuestionPrompt(String prompt) {
        String ls = lineSeparator();
        println(ls + ls + prompt);
    }

    public void displayQuestionPrompt(String[] prompt) {
        String ls = lineSeparator();

        // Initialize string builder with two line separators.
        StringBuilder sb = new StringBuilder(ls);

        // Append question prompt.
        for (String s : prompt)
            sb.append(ls).append(s);

        println(sb.toString());
    }

    public void displayQuestionChoiceList(ChoiceList choiceList) {
        int i;
        String choice;
        char choiceChar = 'A';

        // Get line separator.
        String ls = lineSeparator();

        // Initialize string builder
        StringBuilder sb = new StringBuilder();

        for (i = 0; i < choiceList.size(); i++) {
            // Get choice.
            choice = choiceList.get(i);

            // Append a line separator after every four choices.
            if ((i + 1) % 5 == 0) {
                sb.append(ls);
            }

            // Append choice string.
            sb.append(choiceChar).append(") ").append(choice).append("  \t");

            choiceChar++;
        }

        // Print choices.
        println(sb.toString());
    }

    public void displayQuestionChoiceSet(List<ChoiceList> choiceSet) {
        int i, j;
        String choice;
        char choiceChar = 'A';
        int choiceNum = 1;

        // Get line separator.
        String ls = lineSeparator();

        // Initialize string builder
        StringBuilder sb = new StringBuilder();

        // Get the size of the largest choice list.
        int size = getLargestChoiceListSize(choiceSet);

        for (i = 0; i < size; i++) {
            for (j = 0; j < choiceSet.size(); j++) {
                // Get choice.
                choice = choiceSet.get(j).get(i);

                // Append choice indent mark.
                if ((j + 1) % 2 == 0) sb.append(choiceNum);
                else sb.append(choiceChar);

                // Append choice.
                sb.append(") ").append(choice).append("  \t");
            }

            // Append a new line after each set.
            sb.append(ls);

            choiceChar++;
            choiceNum++;
        }

        // Print choices.
        print(sb.toString());
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

    public void displaySurveyName(String surveyName) {
        println(lineSeparator() + surveyName);
    }

    public void displayQuestionResponse(String response) {
        println(response);
    }

    public void displayQuestionResponse(String response, boolean lineSepBefore, boolean lineSepAfter) {
        String line = "";

        // Test line separator before response.
        if (lineSepBefore)
            line += lineSeparator();

        // Append response.
        line += response;

        // Test line separator after response.
        if (lineSepAfter)
            println(line);
        else
            print(line);
    }

    public void displaySurveyResponse(String response) {
        println(response);
    }
}
