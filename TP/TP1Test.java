import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TP1Test {
    @BeforeEach
    public void setUp() {
        // Initialize the static variables in TP1
        TP1.n = 5;
        TP1.p = new int[] { 1, 3, 5, 7, 9 };
    }

    @Test
    public void testS_TargetLessThanAll() {
        int target = 0;
        int expected = 1; // Closest to 0 is 1
        int result = TP1.S(target);
        assertEquals(expected, result);
    }

    @Test
    public void testS_TargetGreaterThanAll() {
        int target = 10;
        int expected = 1; // Closest to 10 is 9
        int result = TP1.S(target);
        assertEquals(expected, result);
    }

    @Test
    public void testS_TargetInMiddle() {
        int target = 4;
        int expected = 1; // Closest to 4 is 3 or 5, both have a difference of 1
        int result = TP1.S(target);
        assertEquals(expected, result);
    }

    @Test
    public void testS_TargetEqualToElement() {
        int target = 5;
        int expected = 0; // Closest to 5 is 5 itself
        int result = TP1.S(target);
        assertEquals(expected, result);
    }

    @Test
    public void testS_SingleElementArray() {
        TP1.n = 1;
        TP1.p = new int[] { 5 };
        int target = 3;
        int expected = 2; // Closest to 3 is 5, difference is 2
        int result = TP1.S(target);
        assertEquals(expected, result);
    }
}