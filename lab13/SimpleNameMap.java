public class SimpleNameMap {

    /* Instance variables here */
    private Entry[] entries;
    private int size;

    public SimpleNameMap() {
        // YOUR CODE HERE
        this.entries = new Entry[26];
        this.size = 0;
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        // YOUR CODE HERE
        // return 0;
        return this.size;
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(String key) {
        // YOUR CODE HERE
        // return false;
        int index = key.charAt(0) - 'A';
        if (index > 25) {
            return false;
        } else {
            return this.entries[index].key.equals(key);
        }
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public String get(String key) {
        // YOUR CODE HERE
        // return null;
        if (!this.containsKey(key)) {
            return null;
        } else {
            int index = key.charAt(0) - 'A';
            return this.entries[index].value;
        }
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(String key, String value) {
        // YOUR CODE HERE
        int index = key.charAt(0) - 'A';
        if (index > 25) {
            return;
        } else {
            this.entries[index].key = key;
            this.entries[index].value = value;
            this.size += 1;
            return;
        }
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public String remove(String key) {
        // YOUR CODE HERE
        // return null;
        if (!this.containsKey(key)) {
            return null;
        } else {
            int index = key.charAt(0) - 'A';
            String value = this.entries[index].value;
            this.entries[index].key = null;
            this.entries[index].value = null;
            this.size -= 1;
            return value;
        }
    }

    private static class Entry {

        private String key;
        private String value;

        Entry(String key, String value) {
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
