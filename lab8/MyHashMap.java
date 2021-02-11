import javax.swing.text.html.HTMLDocument;
import java.util.*;

public class MyHashMap<K, V> implements Map61B<K, V> {
    private int initialSize = 16;
    private double loadFactor = 0.75;
    private int bucketSize = initialSize;
    private List<Node<K, V>>[] bucket;
    private Set<K> keySet;
    private int size;

    public MyHashMap() {
        this.bucket = initializeBucket(this.bucketSize);
        this.keySet = new HashSet<>();
        this.size = 0;
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        this.bucketSize = initialSize;
        this.bucket = initializeBucket(this.bucketSize);
        this.keySet = new HashSet<>();
        this.size = 0;
        this.size = 0;
    }

    public MyHashMap(int initialSize, double loadFactor) {
        this.initialSize = initialSize;
        this.loadFactor = loadFactor;
        this.bucketSize = initialSize;
        this.bucket = initializeBucket(this.bucketSize);
        this.keySet = new HashSet<>();
        this.size = 0;
    }

    @Override
    public void clear() {
        this.initialSize = 16;
        this.loadFactor = 0.75;
        this.bucketSize = initialSize;
        this.bucket = initializeBucket(this.bucketSize);
        this.keySet = new HashSet<>();
        this.size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        int bucketIndex = this.hash(key);
        for (Node n : bucket[bucketIndex]) {
            if (n.getKey().equals(key)) {
                return (V) n.getValue();
            }
        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = this.hash(key);
        Node newNode = new Node(key, value);
        boolean exist = false;
        for (Node n : bucket[bucketIndex]) {
            if (n.getKey().equals(key)) {
                n.setValue(value);
                exist = true;
            }
        }
        if (!exist) {
            this.bucket[bucketIndex].add(newNode);
            this.size += 1;
            this.keySet.add((K) key);
        }
        if ((double) this.size / this.bucketSize > this.loadFactor) {
            resize(this.bucketSize * 2);
        }
    }

    @Override
    public Set keySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Iterator iterator() {
        return keySet.iterator();
    }


    //    Helper method
    private class Node<K, V> {
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public Node<K, V> clone() {
            Node<K, V> node = new Node<>(this.key, this.value);
            return node;
        }
    }

    private void resize(int newSize) {
        List<Node<K, V>>[] newBucket = initializeBucket(newSize);
        List<Node<K, V>> items = this.items();
        for (Node<K, V> item : items) {
            newBucket[this.hash(item.getKey())].add(item.clone());
        }
        this.bucket = newBucket;
        this.bucketSize = newSize;
    }

    private List<Node<K, V>> items() {
        List<Node<K, V>> items = new LinkedList<>();
        for (List<Node<K, V>> l : this.bucket) {
            for (Node n : l) {
                items.add(n.clone());
            }
        }
        return items;
    }

    private List<Node<K, V>>[] initializeBucket(int bucketSize) {
        List<Node<K, V>>[] returnBucket = new LinkedList[bucketSize];
        for (int i = 0; i < returnBucket.length; i++) {
            returnBucket[i] = new LinkedList<>();
        }
        return returnBucket;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % this.bucketSize);
    }
}
