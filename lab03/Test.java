import java.awt.*;

class Point {
	int x, y;
	
	public Point() {
		
	}
}

public class Test {
	
	public static void main(String[] args) {
		Point[] line1, line2;
		line1 = new Point[2];
		line2 = new Point[2];
		line1[0] = new Point();
		line1[1] = new Point();
		
		line1[0].x = 1;
		line1[0].y = 3;
		line1[1].x = 7;
		line1[1].y = 9;
		line2[0] = line1[1];
		line2[1] = line1[0];
		
		line1[0].x = 11;
		line1[1] = line1[0];
		System.out.println(line2[0].x + " " + line2[0].y + " " + line2[1].x + " " + line2[1].y);
		System.out.println(line1[0] + ", " + line1[1] + ", " + line2[0] + ", " + line2[1]);
	}
}