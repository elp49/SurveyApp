package survey.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionResponse implements Serializable {
    protected final List<String> responseList;

    public QuestionResponse() {
        responseList = new ArrayList<>();
    }

    public List<String> getResponseList() {
        return responseList;
    }

    public boolean add(String s) {
        return responseList.add(s);
    }

    public void clear() {
        responseList.clear();
    }

    public String get(int index) {
        return responseList.get(index);
    }

    public void add(int index, String s) {
        responseList.add(index, s);
    }
}
