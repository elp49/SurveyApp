package survey.response;

import java.io.Serializable;
import java.util.List;

public class QuestionResponse implements Serializable {
    protected String questionType;
    protected final List<String> responseList;

    public QuestionResponse(String questionType, List<String> responseList) {
        this.questionType = questionType;
        this.responseList = responseList;
    }

    public boolean add(String s) {
        return responseList.add(s);
    }

    public String get(int index) {
        return responseList.get(index);
    }
}
