package survey.io;

import java.util.List;

public interface SurveyInputReader {
    String readValidMenuChoice(List<String> options);

    Integer readMenuChoice();

    String readQuestionPrompt();

    Integer readQuestionChoiceCount();

    String readQuestionChoice();

    String readQuestionResponse();
}
