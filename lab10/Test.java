import java.util.*;

public class Test implements Comparator<Integer> {
	
	public int compare(Integer o1, Integer o2) {
		return Integer.compare(Math.abs(o1 - 42), Math.abs(o2 - 42));
	}
	
	public static int compare42(Integer i1, Integer i2) {
		return Integer.compare(Math.abs(i1 - 42), Math.abs(i2 - 42));
	}
	
	public static void main(String[] args) {
		Integer[] arrComparator = {0, 32, 42, 52, 83};
		Arrays.sort(arrComparator, new Test());
		System.out.println(Arrays.toString(arrComparator));
	}
}