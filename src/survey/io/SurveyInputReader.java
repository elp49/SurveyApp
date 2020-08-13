package survey.io;

import java.util.List;

public interface SurveyInputReader {
    String readValidMenuChoice(List<String> options);

    String readValidMenuChoice(List<String> options, int offset);

    Integer readMenuChoice();

    String readQuestionPrompt();

    Integer readQuestionChoiceCount();

    String readQuestionChoice();

    String readQuestionResponse();
}
