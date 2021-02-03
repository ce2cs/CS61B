package es.datastructur.synthesizer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first = 0;
        last = 0;
        fillCount = 0;
        rb = (T[]) new Object[capacity];
    }

    @Override
    public int capacity() {
        return rb.length;
    }

    @Override
    public int fillCount() {
        return fillCount;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */

    @Override
    public void enqueue(T x) {
        if (this.isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        fillCount += 1;
        moveLast();
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (this.isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T returnItem = rb[first];
        rb[first] = null;
        fillCount -= 1;
        moveFirst();
        return returnItem;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (this.isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return rb[first];
    }

    private class BQIterator<T> implements Iterator<T> {
        private int index = first;

        @Override
        public boolean hasNext() {
            return index != last;
        }

        @Override
        public T next() {
            int currentIndex = index;
            index = moveIndex(index);
            return (T) rb[currentIndex];
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new BQIterator<T>();
    }

    @Override
    public boolean equals(Object o) {
        int currentFirst = first;
        int currentLast = last;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayRingBuffer<?> that = (ArrayRingBuffer<?>) o;
        if (fillCount == that.fillCount) {
            for (int i = 0; i < fillCount; i++) {
                if (peek().equals(that.peek())) {
                    moveFirst();
                    that.moveFirst();
                } else {
                    return false;
                }
            }
        }
        first = currentFirst;
        last = currentLast;
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(first, last, fillCount);
        result = 31 * result + Arrays.hashCode(rb);
        return result;
    }

    public void initAsZeros() {
        Arrays.fill(rb, 0);
    }

    private void moveLast() {
        if (last == capacity() - 1) {
            last = 0;
        } else {
            last += 1;
        }
    }

    private void moveFirst() {
        if (first == capacity() - 1) {
            first = 0;
        } else {
            first += 1;
        }
    }

    private int moveIndex(int index) {
        if (index == capacity() - 1) {
            return 0;
        } else {
            return index + 1;
        }
    }
}

