public class ArrayDeque<T> implements Deque<T> {

	private T[] items;
	private int head, tail, size, capacity;
	private double numUse;
	
	public ArrayDeque() {
		this.size = 0;
		this.items = (T[]) new Object[2];
		this.capacity = this.items.length;
		this.head = this.capacity - 1;
		this.tail = this.size;
		this.numUse = Double.valueOf(this.size) / Double.valueOf(this.capacity);
	}
	
	private int minusOne(int index) {
		if (index == 0) {
			return this.capacity - 1;
		} else {
			return index - 1;
		}
	}
	
	private int plusOne(int index) {
		if (index == this.capacity - 1) {
			return 0;
		} else {
			return index + 1;
		}
	}
	
	private void resize(int multiplier) {
		T[] newArray = (T[]) new Object[multiplier];
		int realHead = plusOne(this.head);
		if (multiplier < this.capacity) {
			if (this.capacity - realHead >= multiplier) {
				System.arraycopy(this.items, realHead, newArray, 0, multiplier);
			} else {
				System.arraycopy(this.items, realHead, newArray, 0, this.capacity - realHead);
				System.arraycopy(this.items, 0, newArray, this.capacity - realHead, multiplier - this.capacity + realHead);
			}
		} else {
			System.arraycopy(this.items, realHead, newArray, 0, this.capacity - realHead);
			System.arraycopy(this.items, 0, newArray, this.capacity - realHead, realHead);
		}
		this.items = newArray;
		this.capacity = this.items.length;
		this.head = this.capacity - 1;
		this.tail = this.size;
	}
	
	public void addFirst(T item) {
		if (this.size < this.capacity) {
			this.items[this.head] = item;
			this.size += 1;
			this.head = minusOne(this.head);
		} else {
			this.resize(2 * this.capacity);
			this.items[this.head] = item;
			this.size += 1;
			this.head = minusOne(this.head);
		}
		this.numUse = Double.valueOf(this.size) / Double.valueOf(this.capacity);
		return;
	}
	
	public void addLast(T item) {
		if (this.size < this.capacity) {
			this.items[this.tail] = item;
			this.size += 1;
			this.tail = plusOne(this.tail);
		} else {
			this.resize(2 * this.capacity);
			this.items[this.tail] = item;
			this.size += 1;
			this.tail = plusOne(this.tail);
		}
		this.numUse = Double.valueOf(this.size) / Double.valueOf(this.capacity);
		return;
	}
	
	/** This method has been realized in the Deque interface
	public boolean isEmpty() {
		return this.size == 0;
	}
	**/
	
	public int size() {
		return this.size;
	}
	
	public void printDeque() {
		for (int i = 0; i < this.size; i++) {
			System.out.print(this.items[plusOne(this.head + i) % this.capacity] + " ");
		}
		return;
	}
	
	public T removeFirst() {
		if (this.items[plusOne(this.head)] == null) {
			return null;
		}
		if (this.numUse < 0.25) {
			this.resize(this.capacity / 2);
		}
		T item = this.items[plusOne(this.head)];
		this.items[plusOne(this.head)] = null;
		this.head = plusOne(this.head);
		this.size -= 1;
		this.numUse = Double.valueOf(this.size) / Double.valueOf(this.capacity);
		return item;
	}
	
	public T removeLast() {
		if (this.items[plusOne(this.head)] == null) {
			return null;
		}
		if (this.numUse < 0.25) {
			this.resize(this.capacity / 2);
		}
		T item = this.items[minusOne(this.tail)];
		this.items[minusOne(this.tail)] = null;
		this.tail = minusOne(this.tail);
		this.size -= 1;
		this.numUse = Double.valueOf(this.size) / Double.valueOf(this.capacity);
		return item;
	}
	
	public T get(int index) {
		if (this.items[(plusOne(this.head) + index) % this.capacity] == null) {
			return null;
		} else {
			return this.items[(plusOne(this.head) + index) % this.capacity];
		}
	}
}
