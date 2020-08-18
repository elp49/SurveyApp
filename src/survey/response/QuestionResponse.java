package survey.response;

import java.io.Serializable;
import java.util.*;

public class QuestionResponse<String> implements Serializable, List<String> {
    protected final List<String> responseList;

    public QuestionResponse() {
        responseList = new ArrayList<>();
    }

    @Override
    public int size() {
        return responseList.size();
    }

    @Override
    public boolean isEmpty() {
        return responseList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return responseList.contains(o);
    }

    @Override
    public Iterator<String> iterator() {
        return responseList.iterator();
    }

    @Override
    public Object[] toArray() {
        return responseList.toArray();
    }

    @Override
    public boolean add(Object o) {
        return responseList.add((String) o);
    }

    @Override
    public boolean remove(Object o) {
        return responseList.remove(o);
    }

    @Override
    public boolean addAll(Collection c) {
        Collection<String> list = new ArrayList<>();
        for (Object o : c)
            list.add((String) o);

        return responseList.addAll(list);
    }

    @Override
    public boolean addAll(int index, Collection c) {
        Collection<String> list = new ArrayList<>();
        for (Object o : c)
            list.add((String) o);

        return responseList.addAll(index, list);
    }

    @Override
    public void clear() {
        responseList.clear();
    }

    @Override
    public String get(int index) {
        return responseList.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return responseList.set(index, (String) element);
    }

    @Override
    public void add(int index, Object element) {
        responseList.add(index, (String) element);
    }

    @Override
    public String remove(int index) {
        return responseList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return responseList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return responseList.lastIndexOf(0);
    }

    @Override
    public ListIterator<String> listIterator() {
        return responseList.listIterator();
    }

    @Override
    public ListIterator<String> listIterator(int index) {
        return responseList.listIterator(index);
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        return responseList.subList(fromIndex, toIndex);
    }

    @Override
    public boolean retainAll(Collection c) {
        Collection<String> list = new ArrayList<>();
        for (Object o : c)
            list.add((String) o);

        return responseList.retainAll(list);
    }

    @Override
    public boolean removeAll(Collection c) {
        Collection<String> list = new ArrayList<>();
        for (Object o : c)
            list.add((String) o);

        return responseList.removeAll(list);
    }

    @Override
    public boolean containsAll(Collection c) {
        return responseList.containsAll(c);
    }

    @Override
    public Object[] toArray(Object[] a) {
        return responseList.toArray(a);
    }
}
