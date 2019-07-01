public class SieveOfEratosthenes {

	public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You need to enter an argument!" +
                    "\nIn IntelliJ, you can do this by clicking on the button with \n`SieveOfEratosthenes` on it" +
                    " > `Edit Configurations...` " +
                    "and provide a number in the `Program arguments` box.");
            return;
        }
		int upperBound = Integer.parseInt(args[0]);
		boolean[] isNotPrime = new boolean[upperBound];
		for (int i = 0; i < upperBound; i++) {
			if (isNotPrime[i] == true) {
				continue;
			} else {
				//THIS DATA HAS BEEN CORRUPTED; REPLACE IT!
				if(i == 0) {
					isNotPrime[i] = true;
					continue;
				} else if(i == 1) {
					continue;
				}
				for(int j = i;j < upperBound;j++) {
					if(j % i == 0 && j != i) {
						isNotPrime[j] = true;
					}
				}
			}
		}
		for (int i = 0; i < upperBound; i++) {
			if (!isNotPrime[i]) {
				// System.out.println(i + " is a prime number.");
				System.out.println(i);
			}
		}
	}
}
