import java.lang.Override;

public class LinkedListDeque<T> implements Deque<T> {
    private int size;
    private ListNode sentinel;

    private class ListNode {
        public T item;
        public ListNode prior;
        public ListNode latter;

        public ListNode(T i, ListNode p, ListNode l) {
            this.item = i;
            this.prior = p;
            this.latter = l;
        }
    }

    public LinkedListDeque() {
        this.sentinel = new ListNode(null, null, null);
        this.sentinel.prior = this.sentinel.latter = this.sentinel;
        this.size = 0;
    }

    public LinkedListDeque(LinkedListDeque other) {
        this.sentinel = new ListNode(null, null, null);
        this.sentinel.prior = this.sentinel.latter;
        this.size = 0;

        for (int i = 0; i < other.size(); i++) {
            addLast((T) other.get(i));
        }
    }

    @Override
    public void addFirst(T i) {
        ListNode tempNode = new ListNode(i, sentinel, this.sentinel.latter);
        this.sentinel.latter.prior = tempNode;
        this.sentinel.latter = tempNode;
        this.size += 1;
    }

    @Override
    public void addLast(T i) {
        ListNode tempNode = new ListNode(i, this.sentinel.prior, this.sentinel);
        this.sentinel.prior.latter = tempNode;
        this.sentinel.prior = tempNode;
        this.size += 1;
    }

    @Override
    public T removeFirst() {
        T returnItem = this.sentinel.latter.item;
        this.sentinel.latter = this.sentinel.latter.latter;
        this.sentinel.latter.prior = this.sentinel;
        this.size -= 1;
        return returnItem;
    }

    @Override
    public T removeLast() {
        T returnItem = this.sentinel.prior.item;
        this.sentinel.prior = this.sentinel.prior.prior;
        this.sentinel.prior.latter = this.sentinel;
        this.size -= 1;
        return returnItem;
    }

    @Override
    public void printDeque() {
        ListNode p = this.sentinel.latter;
        while (p.item != null) {
            System.out.print(p.item.toString());
            System.out.print(" ");
            p = p.latter;
        }
        System.out.println();
    }

    @Override
    public T get(int index) {
        int i;
        ListNode p = null;
        if (index < size / 2) {
            p = this.sentinel.latter;
            for (i = 0; i < index; i++) {
                p = p.latter;
            }
        } else {
            p = this.sentinel.prior;
            for (i = size - 1; i > index; i--) {
                p = p.prior;
            }
        }
        return p.item;
    }

    @Override
    public T getRecursive(int index) {
        if (index == 0) {
            return this.sentinel.latter.item;
        }
        LinkedListDeque<T> deque = new LinkedListDeque<>();
        deque.sentinel = this.sentinel.latter;
        return deque.getRecursive(index - 1);
    }

    @Override
    public int size() {
        return size;
    }

}
