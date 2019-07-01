public class MainTest {
	
	public static void main(String[] args) {
		Dog d = new Corgi();
		Corgi c = new Corgi();
		Dog d2 = new Dog();
		// Corgi c2 = new Dog();
		// Corgi c3 = (Corgi) new Dog();
		
		// d.play(d);
		// d.play(c);
		c.play(d);
		c.play(c);
		c.bark(d);
		c.bark(c);
		d.bark(d);
		d.bark(c);
		// d.bark((int) c);
		// c.bark((Corgi) d2);
		((Corgi) d).bark(c);
		((Dog) c).bark(c);
		c.bark((Dog) c);
	}
}