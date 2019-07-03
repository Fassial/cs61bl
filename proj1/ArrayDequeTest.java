import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void yourTestHere() {

    }
	
	@Test
    public void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        // System.out.println("Make sure to uncomment the lines below (and delete this line).");
        
        ArrayDeque<String> adl = new ArrayDeque<>();

        // Java will try to run the below code.
        // If there is a failure, it will jump to the finally block before erroring.
        // If all is successful, the finally block will also run afterwards.
        try {

            assertTrue(adl.isEmpty());

            adl.addFirst("front");
            assertEquals(1, adl.size());
            assertFalse(adl.isEmpty());

            adl.addLast("middle");
            assertEquals(2, adl.size());

            adl.addLast("back");
            assertEquals(3, adl.size());

        } finally {
            // The deque will be printed at the end of this test
            // or after the first point of failure.
            System.out.println("Printing out deque: ");
            adl.printDeque();
        }
    }

    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    @Test
    public void addRemoveTest() {
        System.out.println("Running add/remove test.");
        // System.out.println("Make sure to uncomment the lines below (and delete this line).");

        ArrayDeque<Integer> adl = new ArrayDeque<>();

        try {
            assertTrue(adl.isEmpty());

            adl.addFirst(10);
            assertFalse(adl.isEmpty());

            adl.removeFirst();
            assertTrue(adl.isEmpty());
        } finally {
            System.out.println("Printing out deque: ");
            adl.printDeque();
        }
    }
}
