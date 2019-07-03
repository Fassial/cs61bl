public class ArrayDeque<T> implements Deque<T> {
	
	private T[] items;
	private int head, tail, size, capacity;
	private double num_use;
	
	public ArrayDeque() {
		this.size = 0;
		this.items = (T[])new Object[2];
		this.capacity = this.items.length;
		this.head = this.capacity - 1;
		this.tail = this.size;
		this.num_use = Double.valueOf(this.size) / Double.valueOf(this.capacity);
	}
	
	private int minus_1(int index) {
		if(index == 0) {
			return this.capacity - 1;
		} else {
			return index - 1;
		}
	}
	
	private int plus_1(int index) {
		if(index == this.capacity - 1) {
			return 0;
		} else {
			return index + 1;
		}
	}
	
	private void resize(int multiplier) {
		T[] newArray = (T[])new Object[multiplier];
		int realHead = plus_1(this.head);
		System.arraycopy(this.items, realHead, newArray, 0, this.capacity - realHead);
		System.arraycopy(this.items, 0, newArray, this.capacity - realHead, realHead);
		this.items = newArray;
		this.capacity = this.items.length;
		this.head = this.capacity - 1;
		this.tail = this.size;
	}
	
	public void addFirst(T item) {
		if(this.size < this.capacity) {
			this.items[this.head] = item;
			this.size += 1;
			this.head = minus_1(this.head);
		} else {
			this.resize(2 * this.capacity);
			this.items[this.head] = item;
			this.size += 1;
			this.head = minus_1(this.head);
		}
		this.num_use = Double.valueOf(this.size) / Double.valueOf(this.capacity);
		return;
	}
	
	public void addLast(T item) {
		if(this.size < this.capacity) {
			this.items[this.tail] = item;
			this.size += 1;
			this.tail = plus_1(this.tail);
		} else {
			this.resize(2 * this.capacity);
			this.items[this.tail] = item;
			this.size += 1;
			this.tail = plus_1(this.tail);
		}
		this.num_use = Double.valueOf(this.size) / Double.valueOf(this.capacity);
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
		for(int i = 0;i < this.size;i++) {
			System.out.print(this.items[plus_1(this.head + i) % this.capacity] + " ");
		}
		return;
	}
	
	public T removeFirst() {
		if(this.items[plus_1(this.head)] == null) {
			return null;
		}
		if(this.num_use < 0.25) {
			this.resize(this.capacity / 2);
		}
		T item = this.items[plus_1(this.head)];
		this.items[plus_1(this.head)] = null;
		this.head = plus_1(this.head);
		this.size -= 1;
		this.num_use = Double.valueOf(this.size) / Double.valueOf(this.capacity);
		return item;
	}
	
	public T removeLast() {
		if(this.items[plus_1(this.head)] == null) {
			return null;
		}
		if(this.num_use < 0.25) {
			this.resize(this.capacity / 2);
		}
		T item = this.items[minus_1(this.tail)];
		this.items[minus_1(this.tail)] = null;
		this.tail = minus_1(this.tail);
		this.size -= 1;
		this.num_use = Double.valueOf(this.size) / Double.valueOf(this.capacity);
		return item;
	}
	
	public T get(int index) {
		if(this.items[(plus_1(this.head) + index) % this.capacity] == null) {
			return null;
		} else {
			return this.items[(plus_1(this.head) + index) % this.capacity];
		}
	}
}	