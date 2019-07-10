public class RuntimeTest {
	
	public static int f7 (int[] array, int start, int end) {
		if (array.length <= 1 || end <= start) {
			return 1;
		} else if (array[start] <= array[end]) {
			return f7(array, start + 1, end - 1);
		} else {
			int tmp = array[start];
			array[start] = array[end];
			array[end] = tmp;
			return f7(array, start + 1, end) + f7(array, start, end - 1) + f7(array, start + 1, end - 1);
		}
	}

	public static void main (String[] args) {
		int[] array = {3, 4, 4, 1, 2};
		int value = f7(array, 0, array.length - 1);
		System.out.println(value);
		for (int i = 0;i < array.length;i++) {
			System.out.print(array[i]);
		}
		System.out.println();
	}
}