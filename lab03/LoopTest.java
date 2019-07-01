public class LoopTest {
	
	public static void main(String[] args) {
		for(int k = 1;k < 10;k = k + 1) {
			k = k + 1;
			System.out.print(k + " ");
		}
		System.out.println();
	}
}