package survey.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionResponse implements Serializable {
    protected final List<String> responseList;

    public QuestionResponse() {
        responseList = new ArrayList<>();
    }

    public boolean add(String s) {
        return responseList.add(s);
    }

    public String get(int index) {
        return responseList.get(index);
    }
}
