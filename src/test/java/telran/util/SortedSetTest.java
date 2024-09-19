package telran.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

//{3, -10, 20, 1, 10, 8, 100 , 17}
public abstract class SortedSetTest extends SetTest {
    SortedSet<Integer> sortedSet;

    @Override
    void setUp() {
        super.setUp();
        sortedSet = (SortedSet<Integer>) collection;

    }

    @Test
    void floorTest() {
        assertEquals(10, sortedSet.floor(10));
        assertNull(sortedSet.floor(-11));
        assertEquals(10, sortedSet.floor(11));
        assertEquals(100, sortedSet.floor(101));
    }

    @Test
    void ceilingTest() {
        assertEquals(10, sortedSet.ceiling(10));
        assertNull(sortedSet.ceiling(101));
        assertEquals(17, sortedSet.ceiling(11));
        assertEquals(-10, sortedSet.ceiling(-11));
    }

    @Test
    void firstTest() {
        assertEquals(-10, sortedSet.first());
        sortedSet.clear();
        assertThrowsExactly(NoSuchElementException.class,
        () -> sortedSet.first());
    }

    @Test
    void lastTest() {
        assertEquals(100, sortedSet.last());
    }

    @Test
    void subSetTest() {
        Integer[] expected = { 10, 17 };
        Integer[] actual = getActualSubSet(10, 20);
        assertArrayEquals(expected, actual);
        actual = getActualSubSet(9, 18);
        assertArrayEquals(expected, actual);
        actual = getActualSubSet(100, 100);
        assertEquals(0, actual.length);
        assertThrowsExactly(IllegalArgumentException.class,
         ()->sortedSet.subSet(10, 5));
       

    }

    private Integer[] getActualSubSet(int keyFrom, int keyTo) {
        return sortedSet.subSet(keyFrom, keyTo).stream().toArray(Integer[]::new);
    }
    @Override
    protected void fillBigCollection(){
        Integer[] array = getBigArrayCW();
        Arrays.stream(array).forEach(collection::add);
    }

    protected Integer[] getBigArrayCW() {
       return new Random().ints().distinct().limit(N_ELEMENTS).boxed().toArray(Integer[]::new);

    }
   
    @Override
    protected void runTest(Integer[] expected) {
        Integer[] expectedSorted = Arrays.copyOf(expected, expected.length);
        Arrays.sort(expectedSorted);
        Integer[] actual = collection.stream().toArray(Integer[]::new);
        
        assertArrayEquals(expectedSorted, actual);
        assertEquals(expected.length, collection.size());
    }

protected Integer[] getBigArrayHW() {
    int[] sortedArray = IntStream.range(0, N_ELEMENTS).toArray();
    return balanceArray(sortedArray, 0, sortedArray.length - 1);
}

private Integer[] balanceArray(int[] sortedArray, int start, int end) {
    if (start > end) {
        return new Integer[0];
    }
    int mid = (start + end) / 2;
    Integer[] left = balanceArray(sortedArray, start, mid - 1);
    Integer[] right = balanceArray(sortedArray, mid + 1, end);

    Integer[] balancedArray = new Integer[left.length + right.length + 1];
    balancedArray[0] = sortedArray[mid];
    System.arraycopy(left, 0, balancedArray, 1, left.length);
    System.arraycopy(right, 0, balancedArray, 1 + left.length, right.length);

    return balancedArray;
}
@Test
void balanceTestHW() {
    Integer[] balancedArray = getBigArrayHW();
    TreeSet<Integer> balancedSet = new TreeSet<>();
    Arrays.stream(balancedArray).forEach(balancedSet::add);

    assertTrue(isBalanced(balancedSet), "The TreeSet is not balanced");
    assertEquals(balancedArray.length, balancedSet.size());
}

private boolean isBalanced(TreeSet<Integer> set) {
    int height = set.height();
    int minHeight = (int) Math.floor(Math.log(set.size()) / Math.log(2));
    return height <= minHeight + 1;
}
}