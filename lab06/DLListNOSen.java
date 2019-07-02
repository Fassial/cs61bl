public class DLListNOSen {

    /**
     * IntListNode is a nested class that represents a single node in the
     * DLListNOSen, storing an item and a reference to the next IntListNode.
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
            if (o == null || this.getClass() != o.getClass()) return false;
			/** Here what the getClass() directly returns is the class of the instance created in the heap! **/
            IntListNode that = (IntListNode) o;
            return this.item == that.item;
        }

        @Override
        public String toString() {
            return item + "";
        }

    }

    /* The first item (if it exists) is at sentinel.next. */
    private IntListNode head;
	private IntListNode tail;
    private int size;

    /** Creates an empty DLListNOSen. */
    public DLListNOSen() {
        this.head = null;
		this.tail = null;
        this.size = 0;
    }

    public DLListNOSen(int x) {
		IntListNode p = new IntListNode(x, null, null);
		this.head = p;
        this.tail = p;
        this.size = 1;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
			return true;
		}
        if(o == null || this.getClass() != o.getClass()) {
			return false;
		}
        DLListNOSen dlListNOSen = (DLListNOSen) o;
        if(this.size != dlListNOSen.size) return false;

        IntListNode l1 = this.head;
        IntListNode l2 = dlListNOSen.head;

        while (l1 != this.tail && l2 != dlListNOSen.tail) {
            if(!l1.equals(l2)) {
				return false;
			}
            l1 = l1.next;
            l2 = l2.next;
        }
		// Only the size != 0, we can judge whether the tail is equal
		if(this.size != 0) {
			if(!l1.equals(l2)) {
				return false;
			}
		}
        return true;
    }

    @Override
    public String toString() {
        IntListNode l = this.head;
        String result = "";

        while (l != this.tail) {
            result += l + " ";
            l = l.next;
        }
		result += l + " ";
        return result.trim();
    }

    /** Returns an DLListNOSen consisting of the given values. */
    public static DLListNOSen of(int... values) {
        DLListNOSen list = new DLListNOSen();
        for (int i = values.length - 1; i >= 0; i -= 1) {
            list.addFirst(values[i]);
        }
        return list;
    }

    /** Returns the size of the list. */
    public int size() {
        return this.size;
    }

    /** Adds x to the front of the list. */
    public void addFirst(int x) {
		IntListNode p = new IntListNode(x, null, null);
		if(this.size == 0) {
			this.head = p;
			this.tail = p;
			this.size += 1;
			return;
		}
		p.next = this.head;
        p.next.prev = p;
		this.head = p;
        this.size += 1;
		return;
    }

    /** Return the value at the given index. */
    public int get(int index) {
        IntListNode p = this.head;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    /** Adds x to the list at the specified index. */
    public void add(int index, int x) {
        // TODO
		/** WRONG METHOD!!!
		if(index == 0) {
			this.addFirst(x);
			return;
		} else if(index >= size) {
			index = size;
		}
		**/
		if(index >= size) {
			index = size;
		}
		if(index == 0) {
			this.addFirst(x);
			return;
		}
		
		IntListNode p = this.head;
		while(index-- >= 2) {
			p = p.next;
		}
		
		if(p == this.tail) {
			IntListNode q = new IntListNode(x, p, null);
			p.next = q;
			this.tail = q;
			this.size += 1;
			return;
		}
		IntListNode q = new IntListNode(x, p, p.next);
		p.next.prev = q;
		p.next = q;
		this.size += 1;
		return;
    }

    /** Destructively reverses this list. */
    public void reverse() {
        // TODO
		IntListNode p = this.head, prehead = p;
		while(p != this.tail) {
			IntListNode q = p.next;
			p.next = p.prev;
			p.prev = q;
			p = q;
		}
		if(this.size != 0) {
			p.next = p.prev;
			p.prev = null;
			prehead.next = null;
		}
		this.head = p;
		this.tail = prehead;
    }
}