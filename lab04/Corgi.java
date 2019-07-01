public class Corgi extends Dog {
	
	public Corgi() {
		System.out.println("Initial as a Corgi!");
	}
	
	public void bark(Corgi c) {
		System.out.println("Corgi " + c + " is barking!");
	}
	
	@Override
	public void bark(Dog d) {
		System.out.println("@Override: Dog " + d + " is barking!");
	}
	
	public void play(Dog d) {
		System.out.println("Dog " + d + " is playing!");
	}
	
	public void play(Corgi c) {
		System.out.println("Corgi " + c + " is playing!");
	}
}