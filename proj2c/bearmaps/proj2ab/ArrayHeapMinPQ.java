package bearmaps.proj2ab;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private ArrayList<PriorityNode> minPQ;
    private HashMap<T, Integer> indexMap;

    public ArrayHeapMinPQ() {
        this.minPQ = new ArrayList<>();
        this.minPQ.add(new PriorityNode());
        this.indexMap = new HashMap<>();
    }

    private class PriorityNode implements Comparable<PriorityNode> {
        private double priority;
        private T item;

        public PriorityNode() {
            this.priority = -1;
            this.item = null;
        }

        public PriorityNode(double priority, T item) {
            this.priority = priority;
            this.item = item;
        }

        public double getPriority() {
            return priority;
        }

        public T getItem() {
            return item;
        }

        @Override
        public int compareTo(PriorityNode n) {
            if (n == null) {
                return -1;
            } else {
                return Double.compare(this.getPriority(), n.getPriority());
            }
        }
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("item already exists");
        }
        this.minPQ.add(new PriorityNode(priority, item));
        this.indexMap.put(item, this.size());
        adjustForward(this.size());
    }

    private void adjustForward(int index) {
        if (this.minPQ.get(getParentIndex(index)).getPriority() > this.minPQ.get(index).getPriority()) {
            swap(getParentIndex(index), index);
            adjustForward(getParentIndex(index));
        }
    }

    private int getParentIndex(int index) {
        return index / 2;
    }

    private void swap(int index1, int index2) {
        this.indexMap.put(this.minPQ.get(index1).getItem(), index2);
        this.indexMap.put(this.minPQ.get(index2).getItem(), index1);
        PriorityNode tempNode = this.minPQ.get(index1);
        this.minPQ.set(index1, this.minPQ.get(index2));
        this.minPQ.set(index2, tempNode);
    }

    @Override
    public boolean contains(T item) {
        return this.indexMap.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (this.size() == 0) {
            throw new NoSuchElementException("the heap is empty");
        }
        return this.minPQ.get(1).getItem();
    }

    @Override
    public T removeSmallest() {
        if (this.size() == 0) {
            throw new NoSuchElementException("the heap is empty");
        }
        T ret = getSmallest();
        this.minPQ.set(1, this.minPQ.get(size()));
        this.minPQ.remove(this.size());
        this.indexMap.remove(ret);
        if (this.size() == 0) {
            return ret;
        }
        adjustBackward(1);
        return ret;
    }

    private void adjustBackward(int index) {
        int leftChildIndex = getLeftChildIndex(index);
        int rightChildIndex = getRightChildIndex(index);
        int swapChildIndex = -1;
        double currentPriority = this.minPQ.get(index).getPriority();
        if (leftChildIndex > this.size()) {
            return;
        } else if (rightChildIndex > this.size()) {
            if (currentPriority < this.minPQ.get(leftChildIndex).getPriority()) {
                return;
            } else {
                swapChildIndex = leftChildIndex;
            }
        } else if (currentPriority < Math.min(this.minPQ.get(leftChildIndex).getPriority(), this.minPQ.get(rightChildIndex).getPriority())) {
            return;
        } else if (this.minPQ.get(leftChildIndex).getPriority() < this.minPQ.get(rightChildIndex).getPriority()) {
            swapChildIndex = leftChildIndex;
        } else {
            swapChildIndex = rightChildIndex;
        }
        if (swapChildIndex > 0) {
            swap(swapChildIndex, index);
            adjustBackward(swapChildIndex);
        }
    }

    private int getLeftChildIndex(int index) {
        return index * 2;
    }

    private int getRightChildIndex(int index) {
        return index * 2 + 1;
    }


    @Override
    public int size() {
        return this.minPQ.size() - 1;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("no such item");
        }
        int changeIndex = this.indexMap.get(item);
        this.minPQ.set(changeIndex, new PriorityNode(priority, item));
        adjustBackward(changeIndex);
        adjustForward(changeIndex);
    }
}
