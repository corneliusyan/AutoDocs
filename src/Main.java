public class Main {
	public static void main(String[] args) {
		System.out.println("Program Start");

		String siteId = "ci papi";
		CRDT crdt = new CRDT(siteId);

		// System.out.println("Insert a at position 0");
		// crdt.localInsert('a', 0);
		// System.out.println("Insert b at position 1");
		// crdt.localInsert('b', 1);
		// System.out.println("Insert c at position 2");
		// crdt.localInsert('c', 1);
		// System.out.println("Insert a at position 3");
		// crdt.localInsert('a', 0);
		for (int i = 0; i < 100; i++) {
			crdt.localInsert('a', 0);
			// System.out.println("Print string");
			// crdt.printString();
		}

		System.out.println("\n\nProgram Finish");
	}
}