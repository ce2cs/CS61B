public class ArrayDeque<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;
    private int maxSize;

    public ArrayDeque() {
        maxSize = 8;
        items = (T[]) new Object[maxSize];
        nextFirst = maxSize - 1;
        nextLast = 0;
        size = 0;
    }

    public ArrayDeque(ArrayDeque other) {
        maxSize = other.maxSize;
        items = (T[]) new Object[other.maxSize];
        nextFirst = other.nextFirst;
        nextLast = other.nextLast;
        size = other.size;
        System.arraycopy(other.items, 0, items, 0, other.maxSize);
    }

    public void addFirst(T item) {
        if (items[nextFirst] != null) {
            this.resize(maxSize * 2);
        }
        items[nextFirst] = item;
        size += 1;
        nextFirst = moveXIndex(nextFirst, -1);
    }

    public void addLast(T item) {
        if (items[nextLast] != null) {
            this.resize(maxSize * 2);
        }
        items[nextLast] = item;
        size += 1;
        nextLast = moveXIndex(nextLast, 1);
    }

    public T removeFirst() {
        int removePos = moveXIndex(nextFirst, 1);
        T returnItem = items[removePos];
        items[removePos] = null;
        nextFirst = removePos;
        size -= 1;
        if (size < maxSize / 4) {
            resize(maxSize / 2);
        }
        return returnItem;
    }


    public T removeLast() {
        int removePos = moveXIndex(nextLast, -1);
        T returnItem = items[removePos];
        items[removePos] = null;
        nextLast = removePos;
        size -= 1;
        if (size < maxSize / 4) {
            resize(maxSize / 2);
        }
        return returnItem;
    }

    public T get(int pos) {
        int getPos = moveXIndex(nextFirst, pos + 1);
        return items[getPos];
    }

    public void resize(int newSize) {
        T[] newItems = (T[]) new Object[newSize];
        int p = nextFirst;
        if (p == maxSize - 1) {
            p = 0;
        } else {
            p += 1;
        }
        for (int i = 0; i < size; i++) {
            newItems[i] = items[p];
            if (p == maxSize - 1) {
                p = 0;
            } else {
                p += 1;
            }
        }
        maxSize = newSize;
        nextFirst = maxSize - 1;
        nextLast = size;
        items = newItems;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int p = nextFirst;
        if (p == maxSize - 1) {
            p = 0;
        } else {
            p += 1;
        }
        while (items[p] != null) {
            System.out.print(items[p].toString());
            System.out.print(" ");
            if (p == maxSize - 1) {
                p = 0;
            } else {
                p += 1;
            }
        }
        System.out.println();
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    private int moveXIndex(int CurrentIndex, int x) {
        int p = CurrentIndex + x;
        if (p >= maxSize) {
            return p - maxSize;
        }
        if (p < 0) {
            return maxSize + p;
        }
        return p;
    }
}
