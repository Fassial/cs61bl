public class IntList {
	
	private int item;
	private IntList next;
	
	public IntList(int item, IntList next) {
		this.item = item;
		this.next = next;
	}
	
	public static IntList skipper(IntList lst) {
		/*
		if(lst == null) {
			return null;
		}
		if(lst.next == null) {
			return skipper(new IntList(lst.item, null));
		}
		return skipper(new IntList(lst.item, lst.next.next));
		*/
		if(lst == null) {
			return null;
		}
		if(lst.next == null) {
			return new IntList(lst.item, null);
		}
		return skipper(new IntList(lst.item, lst.next.next).next);
	}
	
	public static void main(String[] args) {
		IntList p = new IntList(5, null);
		p = new IntList(4, p);
		p = new IntList(3, p);
		p = new IntList(2, p);
		p = new IntList(1, p);
		IntList pp = p;
		while(pp != null) {
			System.out.print(pp.item);
			pp = pp.next;
		}
		System.out.println();
		IntList q = skipper(p);
		while(q != null) {
			System.out.print(q.item);
			q = q.next;
		}
		System.out.println();
	}
}