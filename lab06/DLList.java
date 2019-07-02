public class DLList {

    /**
     * IntListNode is a nested class that represents a single node in the
     * DLList, storing an item and a reference to the next IntListNode.
     */
    private static class IntListNode {
        /*
         * The access modifiers inside a private nested class are irrelevant:
         * both the inner class and the outer class can access these instance
         * variables and methods.
         */
        public int item;
		public IntListNode prev;
        public IntListNode next;

        public IntListNode(int item, IntListNode prev, IntListNode next) {
            this.item = item;
			this.prev = prev;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
			/** Here what the getClass() directly returns is the class of the instance created in the heap! **/
            IntListNode that = (IntListNode) o;
            return item == that.item;
        }

        @Override
        public String toString() {
            return item + "";
        }

    }

    /* The first item (if it exists) is at sentinel.next. */
    private IntListNode sentinel;
    private int size;

    /** Creates an empty DLList. */
    public DLList() {
        sentinel = new IntListNode(42, null, null);
		sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public DLList(int x) {
        sentinel = new IntListNode(42, null, null);
		IntListNode p = new IntListNode(x, null, null);
		sentinel.prev = p;
		p.prev = sentinel;
        sentinel.next = p;
        p.next = sentinel;
        size = 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DLList dlList = (DLList) o;
        if (size != dlList.size) return false;

        IntListNode l1 = sentinel.next;
        IntListNode l2 = dlList.sentinel.next;

        while (l1 != sentinel && l2 != dlList.sentinel) {
            if (!l1.equals(l2)) return false;
            l1 = l1.next;
            l2 = l2.next;
        }
        return true;
    }

    @Override
    public String toString() {
        IntListNode l = sentinel.next;
        String result = "";

        while (l != sentinel) {
            result += l + " ";
            l = l.next;
        }

        return result.trim();
    }

    /** Returns an DLList consisting of the given values. */
    public static DLList of(int... values) {
        DLList list = new DLList();
        for (int i = values.length - 1; i >= 0; i -= 1) {
            list.addFirst(values[i]);
        }
        return list;
    }

    /** Returns the size of the list. */
    public int size() {
        return size;
    }

    /** Adds x to the front of the list. */
    public void addFirst(int x) {
		IntListNode p = new IntListNode(x, sentinel, sentinel.next);
		sentinel.next.prev = p;
        sentinel.next = p;
        size += 1;
    }

    /** Return the value at the given index. */
    public int get(int index) {
        IntListNode p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    /** Adds x to the list at the specified index. */
    public void add(int index, int x) {
        // TODO
		if(index >= size) {
			index = size;
		}
		IntListNode p = sentinel;
		while(index-- >= 1) {
			p = p.next;
		}
		IntListNode q = new IntListNode(x, p, p.next);
		p.next.prev = q;
		p.next = q;
		this.size += 1;
    }

    /** Destructively reverses this list. */
    public void reverse() {
        // TODO
		IntListNode p = this.sentinel;
		while(p.next != sentinel) {
			IntListNode q = p.next;
			p.next = p.prev;
			p.prev = q;
			p = q;
		}
    }
}