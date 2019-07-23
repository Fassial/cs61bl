import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K, V> implements Map61BL<K, V> {
    private LinkedList<Entry<K, V>>[] entries;
    private int size;
    private float loadFactor;
    
    public HashMap() {
        this.entries = new LinkedList[16];
        for (int i = 0; i < this.entries.length; i++) {
            this.entries[i] = new LinkedList<Entry<K, V>>();
        }
        this.size = 0;
        this.loadFactor = 0.75f;
    }
    
    public HashMap(int initialSize) {
        this.entries = new LinkedList[initialSize];
        for (int i = 0; i < this.entries.length; i++) {
            this.entries[i] = new LinkedList<Entry<K, V>>();
        }
        this.size = 0;
        this.loadFactor = 0.75f;
    }
    
    public HashMap(int initialSize, float loadFactor) {
        this.entries = new LinkedList[initialSize];
        for (int i = 0; i < this.entries.length; i++) {
            this.entries[i] = new LinkedList<Entry<K, V>>();
        }
        this.size = 0;
        this.loadFactor = loadFactor;
    }
    
    public int capacity() {
        return this.entries.length;
    }
    
    /* Removes all of the mappings from this map. */
    public void clear() {
        int entriesLen = this.capacity();
        this.entries = new LinkedList[entriesLen];
        this.size = 0;
    }
    
    /* Returns true if this map contains a mapping for the specified key KEY. */
    public boolean containsKey(K key) {
        int index = Math.floorMod(key.hashCode(), this.capacity());
        LinkedList<Entry<K, V>> list = this.entries[index];
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).key.equals(key)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
    
     /* Returns the value to which the specified key KEY is mapped, or null if
       this map contains no mapping for KEY. */
    public V get(K key) {
        int index = Math.floorMod(key.hashCode(), this.capacity());
        LinkedList<Entry<K, V>> list = this.entries[index];
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).key.equals(key)) {
                    return (V) list.get(i).value;
                }
            }
            return null;
        } else {
            return null;
        }
    }
    
    /* Puts the specified key-value pair (KEY, VALUE) in this map. */
    public void put(K key, V value) {
        if ((float) (this.size() + 1) / (float) this.capacity() > this.loadFactor) {
            int origincapacity = this.capacity();
            LinkedList<Entry<K, V>>[] copy = this.entries;
            this.entries = new LinkedList[origincapacity * 2];
            for (int i = 0; i < this.entries.length; i++) {
                this.entries[i] = new LinkedList<Entry<K, V>>();
            }
            for (int i = 0; i < copy.length; i++) {
                int linkedIndex = 0;
                while (linkedIndex < copy[i].size() && copy[i].get(linkedIndex) != null) {
                    Entry add = new Entry(copy[i].get(linkedIndex).key,
                                    copy[i].get(linkedIndex).value);
                    int index = Math.floorMod(add.key.hashCode(), this.entries.length);
                    this.entries[index].addLast(add);
                    linkedIndex += 1;
                }
            }
            int keyIndex = Math.floorMod(key.hashCode(), this.entries.length);
            this.entries[keyIndex].addLast(new Entry<K, V>(key, value));
            this.size += 1;
            return;
        } else {
            int index = Math.floorMod(key.hashCode(), this.capacity());
            LinkedList<Entry<K, V>> list = this.entries[index];
            if (list != null) {
                int i = 0;
                boolean match = false;
                while (i < list.size()) {
                    if (list.get(i).key.equals(key)) {
                        match = true;
                        list.set(i, new Entry(key, value));
                    }
                    i++;
                }
                if (!match) {
                    list.addLast(new Entry(key, value));
                    size += 1;
                    return;
                }
            } else {
                list = new LinkedList<>();
                list.add(new Entry(key, value));
                this.size += 1;
                return;
            }
        }
    }
    
    /* Removes and returns a key KEY and its associated value. */
    public V remove(K key) {
        int index = Math.floorMod(key.hashCode(), this.capacity());
        LinkedList<Entry<K, V>> list = this.entries[index];
        if (list != null) {
            int i = 0;
            while (i < list.size()) {
                if (list.get(i).key.equals(key)) {
                    V returnValue = list.get(i).value;
                    list.remove(list.get(i));
                    size -= 1;
                    return returnValue;
                }
            }
            return null;
        }
        return null;
    }
    
    public boolean remove(K key, V value) {
        return this.remove(key).equals(value);
    }
    
    /* Returns the number of key-value pairs in this map. */
    public int size() {
        return this.size;
    }

    /* Returns an Iterator over the keys in this map. */
    public Iterator<K> iterator() {
        return new HashMapIterator();
    }
    
    private class HashMapIterator implements Iterator<K> {
        int index;
        int linkedIndex;

        HashMapIterator() {
            this.index = 0;
            this.linkedIndex = 0;
        }
        
        public boolean hasNext() {
            if (this.index < entries.length) {
                int listInd = this.linkedIndex;
                if (listInd < entries[this.index].size()) {
                    if (entries[this.index].get(listInd) != null) {
                        return true;
                    }
                }
            } else {
                return false;
            }
            if (this.index < entries.length - 1) {
                for (int i = this.index + 1; i < entries.length; i++) {
                    if (entries[i].size() > 0) {
                        if (entries[i].get(0) != null) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        
        @Override
        public K next() {
            if (!this.hasNext()) {
                return null;
            } else {
                if (this.linkedIndex < entries[this.index].size()) {
                    K key = entries[index].get(this.linkedIndex).key;
                    this.linkedIndex += 1;
                    return key;
                } else {
                    this.index += 1;
                    this.linkedIndex = 0;
                    if (!hasNext()) {
                        return null;
                    }
                    return this.next();
                }
            }
        }
    }
    
    private static class Entry<K, V> {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
