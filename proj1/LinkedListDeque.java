public class LinkedListDeque<T> implements Deque<T> {

	private class DequeNode {
		private T item;
		private DequeNode prev, next;
		
		public DequeNode(T item, DequeNode prev, DequeNode next) {
			this.item = item;
			this.prev = prev;
			this.next = next;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || this.getClass() != o.getClass()) {
				return false;
			}
			DequeNode that = (DequeNode) o;
			return this.item == that.item;
		}
		
		@Override
		public String toString() {
			return this.item + "";
		}
	}
	
	private int size;
	private DequeNode head, tail;
	
	public LinkedListDeque() {
		this.size = 0;
		this.head = null;
		this.tail = null;
	}
	
	public void addFirst(T item) {
		DequeNode p = new DequeNode(item, null, null);
		if (this.size == 0) {
			this.head = p;
			this.tail = p;
			this.size += 1;
			return;
		} else {
			this.head.prev = p;
			p.next = this.head;
			this.head = p;
			this.size += 1;
			return;
		}
	}
	
	public void addLast(T item) {
		DequeNode p = new DequeNode(item, null, null);
		if (this.size == 0) {
			this.head = p;
			this.tail = p;
			this.size += 1;
			return;
		} else {
			this.tail.next = p;
			p.prev = this.tail;
			this.tail = p;
			this.size += 1;
			return;
		}
	}
	
	/** gradescope ask me to remove it or make it private **/
	/*@Override
	private boolean isEmpty() {
		return this.size == 0;
	}*/
	
	public int size() {
		return this.size;
	}
	
	public void printDeque() {
		if (this.head == null) {
			return;
		}
		DequeNode p = this.head;
		while (p != this.tail) {
			System.out.print(p + " ");
			p = p.next;
		}
		System.out.print(p);
		return;
	}
	
	public T removeFirst() {
		if (this.size == 0) {
			return null;
		} else {
			DequeNode p = this.head;
			if (this.size != 1) {
				this.head = this.head.next;
				this.head.prev = null;
			} else {
				this.head = null;
			}
			this.size -= 1;
			if (this.size == 0) {
				this.tail = this.head;
			}
			return p.item;
		}
	}
	
	public T removeLast() {
		if (this.size == 0) {
			return null;
		} else {
			DequeNode p = this.tail;
			if (this.size != 1) {
				this.tail = this.tail.prev;
				this.tail.next = null;
			} else {
				this.tail = null;
			}
			this.size -= 1;
			if (this.size == 0) {
				this.head = this.tail;
			}
			return p.item;
		}
	}
	
	public T get(int index) {
		if (index >= this.size) {
			return null;
		}
		DequeNode p = this.head;
		while (index-- >= 1) {
			p = p.next;
		}
		return p.item;
	}
	
	private T helperMethod(DequeNode p, int index, int count) {
		if (count == index) {
			return p.item;
		} else {
			return this.helperMethod(p.next, index, count + 1);
		}
	}
	
	public T getRecursive(int index) {
		if (index >= this.size) {
			return null;
		}
		return this.helperMethod(this.head, index, 0);
	}
}		
