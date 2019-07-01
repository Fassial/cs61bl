public class Main {

    public static void makeDinner(Fish f) {
        f.fry();
    }

    public static void makeSecondDinner(Salmon f) {
        f.fry();
    }

    public static void main(String[] args) {
        /*Fish f = new Fish(10);
        Salmon s = new Salmon();
        System.out.println(f.weight);*/

        /** Worksheet: Fish and Salmon 1.2 Code below */
		/*Fish fs = new Salmon(); //Line A
		makeSecondDinner(fs);   //Line B*/
		Fish fish = new Fish();
		Salmon salmon = new Salmon();
		Fish bob = new Salmon();
		
		/*fish.swim();
		salmon.swim();
		bob.swim();
		fish.swim(5);*/
		salmon.swim(5);
    }
}
