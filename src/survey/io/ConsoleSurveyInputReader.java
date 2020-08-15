package survey.io;

import utils.ConsoleInputReader;
import utils.InputReader;

import java.io.IOException;
import java.util.List;

public class ConsoleSurveyInputReader implements SurveyInputReader {
    private final InputReader in;

    public ConsoleSurveyInputReader() {
        in = new ConsoleInputReader();
    }

    private String readln() {
        String line;

        try {
            line = in.readln().trim();
        } catch (IOException e) {
            e.printStackTrace();
            line = null;
        }

        return line;
    }

    private Integer readInteger() {
        Integer num;

        try {
            num = in.readInteger();
        } catch (IOException e) {
            e.printStackTrace();
            num = null;
        }

        return num;
    }

    private boolean isNullOrEmpty(List<String> list) {
        return list == null || list.isEmpty();
    }

    public String readValidMenuChoice(List<String> options) {
        Integer choiceNum;
        String choiceStr = "";

        if (!isNullOrEmpty(options)) {
            // Get user menu choice.
            choiceNum = readMenuChoice();
            if (choiceNum != null) {
                try {
                    // Get choice string.
                    choiceStr = options.get(choiceNum - 1);
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }

        return choiceStr;
    }

    public Integer readMenuChoice() {
        return readInteger();
    }

    public String readQuestionPrompt() {
        return readln();
    }

    public Integer readQuestionChoiceCount() {
        return readInteger();
    }

    public String readQuestionChoice() {
        return readln();
    }

    public String readQuestionResponse() {
        return readln();
    }
}
