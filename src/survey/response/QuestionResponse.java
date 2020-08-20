package survey.response;

import java.io.Serializable;
import java.util.*;

public class QuestionResponse implements Serializable {
    protected final List<String> responseList;

    public QuestionResponse() {
        responseList = new ArrayList<>();
    }

    public List<String> getResponseList() {
        return responseList;
    }

    public int size() {
        return responseList.size();
    }

    public boolean isEmpty() {
        return responseList.isEmpty();
    }

    public boolean contains(String s) {
        return responseList.contains(s);
    }

    public Iterator<String> iterator() {
        return responseList.iterator();
    }

    public Object[] toArray() {
        return responseList.toArray();
    }

    public boolean add(String s) {
        return responseList.add(s);
    }

    public boolean remove(String s) {
        return responseList.remove(s);
    }

    public boolean addAll(Collection<String> c) {
        return responseList.addAll(c);
    }

    public boolean addAll(int index, Collection<String> c) {
        return responseList.addAll(index, c);
    }

    public void clear() {
        responseList.clear();
    }

    public String get(int index) {
        return responseList.get(index);
    }

    public String set(int index, String s) {
        return responseList.set(index, s);
    }

    public void add(int index, String s) {
        responseList.add(index, s);
    }

    public String remove(int index) {
        return responseList.remove(index);
    }

    public int indexOf(String s) {
        return responseList.indexOf(s);
    }

    public int lastIndexOf(String s) {
        return responseList.lastIndexOf(s);
    }

    public ListIterator<String> listIterator() {
        return responseList.listIterator();
    }

    public ListIterator<String> listIterator(int index) {
        return responseList.listIterator(index);
    }

    public List<String> subList(int fromIndex, int toIndex) {
        return responseList.subList(fromIndex, toIndex);
    }

    public boolean retainAll(Collection<String> c) {
        return responseList.retainAll(c);
    }

    public boolean removeAll(Collection<String> c) {
        return responseList.removeAll(c);
    }

    public boolean containsAll(Collection<String> c) {
        return responseList.containsAll(c);
    }

    public Object[] toArray(String[] a) {
        return responseList.toArray(a);
    }
}
