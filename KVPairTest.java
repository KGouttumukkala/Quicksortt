import student.TestCase;

/**
 * The KVPairTest class is a JUnit test case for testing the KVPair class.
 * It contains test methods to verify the functionality of getKey, getValue, and
 * toString methods
 * of the KVPair class.
 * 
 * @author Kiran Gouttumukkala
 * @version 1.0
 */
public class KVPairTest extends TestCase {

    private KVPair<Integer, Integer> test;

    /**
     * Set up the test fixture.
     */
    public void setUp() {
        test = new KVPair<>(42, 100); // Example using integers
    }


    /**
     * Test method for getting the key from KVPair.
     */
    public void testGetKey() {
        assertEquals((Integer)42, (Integer)test.getKey(), 1);
    }


    /**
     * Test method for getting the value from KVPair.
     */
    public void testGetValue() {
        assertEquals((Integer)100, (Integer)test.getValue(), 1);
    }


    /**
     * Test method for converting KVPair to String.
     */
    public void testToString() {
        assertEquals("(42, 100)", test.toString());
    }
}
