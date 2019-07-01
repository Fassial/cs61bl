/** A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 *
 * @author Maurice Lee and Wan Fung Chui
 */

public class IntList {

    /** The integer stored by this node. */
    public int item;
    /** The next node in this IntList. */
    public IntList next;

    /** Constructs an IntList storing ITEM and next node NEXT. */
    public IntList(int item, IntList next) {
        this.item = item;
        this.next = next;
    }

    /** Constructs an IntList storing ITEM and no next node. */
    public IntList(int item) {
        this(item, null);
    }

    /** Returns an IntList consisting of the elements in ITEMS.
     * IntList L = IntList.list(1, 2, 3);
     * System.out.println(L.toString()) // Prints 1 2 3 */
    public static IntList of(int... items) {
        /** Check for cases when we have no element given. */
        if (items.length == 0) {
            return null;
        }
        /** Create the first element. */
        IntList head = new IntList(items[0]);
        IntList last = head;
        /** Create rest of the list. */
        for (int i = 1; i < items.length; i++) {
            last.next = new IntList(items[i]);
            last = last.next;
        }
        return head;
    }

    /**
     * Returns [position]th item in this list. Throws IllegalArgumentException
     * if index out of bounds.
     *
     * @param position, the position of element.
     * @return The element at [position]
     */
    public int get(int position) {
        // YOUR CODE HERE
		IntList p = this;
		int index = 0;
		while(p != null) {
			if(index == position) {
				return p.item;
			}
			p = p.next;
			index++;
		}
		throw new IllegalArgumentException("Position out of range!");
        // return -1;
    }

    /**
     * Returns the string representation of the list. For the list (1, 2, 3),
     * returns "1 2 3".
     *
     * @return The String representation of the list.
     */
    public String toString() {
        // YOUR CODE HERE
		String str = "";
		IntList p = this;
		while(p.next != null) {
			// str += p.item.toString() + " ";		WRONG!!!
			/**	Examoples:
			 * 		String.valueOf(i)
			 * 		Integer.toString(i)
			 * 		i + ""
			**/
			str += p.item + " ";
			p = p.next;
		}
		str += p.item + "";
		return str;
        //return null;
    }

    /**
     * Returns whether this and the given list or object are equal.
     *
     * @param obj, another list (object)
     * @return Whether the two lists are equal.
     */
    public boolean equals(Object obj) {
        // YOUR CODE HERE
		IntList p = this;
		IntList q = null;
		/** How to judge if q is a instance of IntList
		 *		q instanceof IntList
		 *		q.getClass() == IntList.getClass()
		 */
		if(obj instanceof IntList) {
			q = (IntList) obj;
		} else {
			// return false;
		}
		while(p != null && q != null) {
			if(p.item != q.item) {
				return false;
			}
			p = p.next;
			q = q.next;
		}
		if((p == null && q != null) || (p != null && q == null)) {
			return false;
		}
		return true;
        // return false;
    }

    /**
     * Adds the given value at the end of the list.
     *
     * @param value, the int to be added.
     */
    public void add(int value) {
        // YOUR CODE HERE
		IntList p = this;
		while(p.next != null) {
			p = p.next;
		}
		IntList tail = new IntList(value);
		p.next = tail;
    }

    /**
     * Returns the smallest element in the list.
     *
     * @return smallest element in the list
     */
    public int smallest() {
        // YOUR CODE HERE
		IntList p = this;
		int smallest = p.item;
		while(p != null) {
			if(smallest > p.item) {
				smallest = p.item;
			}
			p = p.next;
		}
		return smallest;
        // return -1;
    }

    /**
     * Returns the sum of squares of all elements in the list.
     *
     * @return The sum of squares of all elements.
     */
    public int squaredSum() {
        // YOUR CODE HERE
		IntList p = this;
		int squaredSum = 0;
		while(p != null) {
			squaredSum += Math.pow(p.item, 2);
			p = p.next;
		}
		return squaredSum;
        // return -1;
    }

    /**
     * Destructively squares each item of the list.
     *
     * @param L list to destructively square.
     */
    public static void dSquareList(IntList L) {
        while (L != null) {
            L.item = L.item * L.item;
            L = L.next;
        }
    }

    /**
     * Returns a list equal to L with all elements squared. Non-destructive.
     *
     * @param L list to non-destructively square.
     * @return the squared list.
     */
    public static IntList squareListIterative(IntList L) {
        if (L == null) {
            return null;
        }
        IntList res = new IntList(L.item * L.item, null);
        IntList ptr = res;
        L = L.next;
        while (L != null) {
            ptr.next = new IntList(L.item * L.item, null);
            L = L.next;
            ptr = ptr.next;
        }
        return res;
    }

    /** Returns a list equal to L with all elements squared. Non-destructive.
     *
     * @param L list to non-destructively square.
     * @return the squared list.
     */
    public static IntList squareListRecursive(IntList L) {
        if (L == null) {
            return null;
        }
        return new IntList(L.item * L.item, squareListRecursive(L.next));
    }

    /**
     * Returns a new IntList consisting of A followed by B,
     * destructively.
     *
     * @param A list to be on the front of the new list.
     * @param B list to be on the back of the new list.
     * @return new list with A followed by B.
     */
    public static IntList dcatenate(IntList A, IntList B) {
        IntList p = A;
		if(p == null) {
			return B;
		}
		while(p.next != null) {
			p = p.next;
		}
		p.next = B;
		return A;
		// return null;
    }

    /**
     * Returns a new IntList consisting of A followed by B,
     * non-destructively.
     *
     * @param A list to be on the front of the new list.
     * @param B list to be on the back of the new list.
     * @return new list with A followed by B.
     */
     public static IntList catenate(IntList A, IntList B) {
        if(A == null) {
			return B;
		} else if(B == null) {
			return A;
		}
		IntList p = A;
		IntList q = new IntList(p.item), head = q;
		while(p.next != null) {
			p = p.next;
			q.next = new IntList(p.item);
			q = q.next;
		}
		p = B;
		q.next = new IntList(p.item);
		q = q.next;
		while(p.next != null) {
			p = p.next;
			q.next = new IntList(p.item);
			q = q.next;
		}
		return head;
		// return null;
     }
}
