import org.junit.Test;
import static org.junit.Assert.*;

public class DLListNOSenTest {

    @Test
    public void testDLListNOSenAdd() {
        DLListNOSen test1 = DLListNOSen.of(1, 3, 5);
        DLListNOSen test2 = new DLListNOSen();

        test1.add(1, 2);
        test1.add(3, 4);
        assertEquals(5, test1.size());
        assertEquals(3, test1.get(2));
        assertEquals(4, test1.get(3));

        test2.add(1, 1);
        assertEquals(1, test2.get(0));
        assertEquals(1, test2.size());

        test2.add(10, 10);
        assertEquals(10, test2.get(1));
        test1.add(0, 0);
        assertEquals(DLListNOSen.of(0, 1, 2, 3, 4, 5), test1);
    }
	
	@Test
    public void testDLListNOSenReverse() {
        DLListNOSen s0 = new DLListNOSen();
        s0.reverse();
        assertEquals(new DLListNOSen(), s0);

        DLListNOSen s1 = DLListNOSen.of(1);
        s1.reverse();
        assertEquals(s1, s1);
        assertEquals(DLListNOSen.of(1), s1);

        DLListNOSen s2 = DLListNOSen.of(1, 2, 3, 4, 5);
        DLListNOSen s3 = DLListNOSen.of(5, 4, 3, 2, 1);
        DLListNOSen s2copy = s2;
        s2.reverse();
        assertEquals(s2, s3);

        DLListNOSen s4 = new DLListNOSen();
        s4.reverse();
        assertEquals(new DLListNOSen(), s4);

        DLListNOSen s5 = DLListNOSen.of(1, 2);
        DLListNOSen s5copy = s5;
        DLListNOSen s6 = DLListNOSen.of(2, 1);
        s5.reverse();
        assertEquals(s5, s6);
        assertNotEquals(s5, DLListNOSen.of(1, 2));
        //assertNotEquals(s5, s5copy);
    }
}
