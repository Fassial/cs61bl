public class TracedPointTest extends PointTest {
	// int x, y;

	public TracedPointTest(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		System.out.println("Init as a TracedPointTest!");
	}
	
	public static void main(String[] args) {
		TracedPointTest tp = new TracedPointTest(1, 2);
	}
}