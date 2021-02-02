public interface Deque <T> {

    void addFirst(T item);
    void addLast(T item);
    T removeFirst();
    T removeLast();
    void printDeque();
    T get(int index);
    T getRecursive(int index);
    int size();

    default boolean isEmpty() {
        return this.size() == 0;
    }
}

