import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTMap<K, V> left;
    private BSTMap<K, V> right;
    private K key;
    private V value;

    public BSTMap() {
        left = null;
        right = null;
        key = null;
        value = null;
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        left = null;
        right = null;
        key = null;
        value = null;
    }


    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        if (this.key == null) {
            return false;
        } else if (key.compareTo(this.key) < 0) {
            if (this.left == null) {
                return false;
            }
            return this.left.containsKey(key);
        } else if (key.compareTo(this.key) > 0) {
            if (this.right == null) {
                return false;
            }
            return this.right.containsKey(key);
        }
        return true;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (this.key == null) {
            return null;
        } else if (key.compareTo(this.key) < 0) {
            if (this.left == null) {
                return null;
            }
            return this.left.get(key);
        } else if (key.compareTo(this.key) > 0) {
            if (this.right == null) {
                return null;
            }
            return this.right.get(key);
        }
        return value;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        if (this.key == null) {
            return 0;
        }
        if (this.left == null && this.right == null) {
            return 1;
        }
        if (this.left == null) {
            return 1 + this.right.size();
        }
        if (this.right == null) {
            return 1 + this.left.size();
        }
        return 1 + this.left.size() + this.right.size();
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (this.key == null) {
            this.key = key;
            this.value = value;
        } else if (key.compareTo(this.key) < 0) {
            if (this.left == null) {
                this.left = new BSTMap<K, V>();
            }
            this.left.put(key, value);
        } else if (key.compareTo(this.key) > 0) {
            if (this.right == null) {
                this.right = new BSTMap<K, V>();
            }
            this.right.put(key, value);
        } else {
            this.value = value;
        }
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }


    public void printInOrder() {

    }


}
