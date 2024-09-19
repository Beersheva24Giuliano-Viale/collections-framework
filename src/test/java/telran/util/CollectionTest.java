package telran.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public abstract class CollectionTest {
    protected static final int N_ELEMENTS = 1_048_575;
    protected Collection<Integer> collection;
    Random random = new Random();
    Integer[] array = {3, -10, 20, 1, 10, 8, 100 , 17};
    void setUp() {
        Arrays.stream(array).forEach(collection::add);
    }
    @Test
    void removeIfTest() {
        assertTrue(collection.removeIf(n -> n % 2 == 0));
        assertFalse(collection.removeIf(n -> n % 2 == 0));
        assertTrue(collection.stream().allMatch(n -> n % 2 != 0));
    }
    @Test
    void clearTest() {
        collection.clear();
        assertTrue(collection.isEmpty());
    }
    @Test
    void addNonExistingTest() {
        assertTrue(collection.add(200));
       
        runTest(new Integer[]{3, -10, 20, 1, 10, 8, 100 , 17, 200});
    }
    @Test
    void addExistingTest() {
        assertTrue(collection.add(17));
        runTest(new Integer[]{3, -10, 20, 1, 10, 8, 100 , 17, 17});
    }
    @Test
    void sizeTest() {
        assertEquals(array.length, collection.size());
    }
@Test
    void iteratorTest() {
        Integer[] actual = new Integer[array.length];
        int index = 0;
        Iterator<Integer> it = collection.iterator();
       
        while(it.hasNext()) {
            actual[index++] = it.next();
        }
        
        assertThrowsExactly(NoSuchElementException.class, it::next );
    }
    @Test
    void removeInIteratorTest(){
        Iterator<Integer> it = collection.iterator();
        assertThrowsExactly(IllegalStateException.class, () -> it.remove());
        Integer n = it.next();
        it.remove();
        it.next();
        it.next();
        it.remove();
        assertFalse(collection.contains(n));
        assertThrowsExactly(IllegalStateException.class, () -> it.remove());

    }

    protected void runTest(Integer[] expected) {
        assertArrayEquals(expected, collection.stream().toArray(Integer[]::new));
        assertEquals(expected.length, collection.size());
    }
    @Test
    void streamTest() {
       runTest(array);
    }
    @Test
    void removeTest() {
        Integer[] expected = {-10, 20, 1,  8, 100 };
        assertTrue(collection.remove(10));
        assertTrue(collection.remove(3));
        assertTrue(collection.remove(17));
        runTest(expected);
        assertFalse(collection.remove(10));
        assertFalse(collection.remove(3));
        assertFalse(collection.remove(17));
        clear();
        runTest(new Integer[0]);

    }
    private void clear() {
        Arrays.stream(array).forEach(n -> collection.remove(n));
    }
    @Test
    void isEmptyTest() {
        assertFalse(collection.isEmpty());
        clear();
        assertTrue(collection.isEmpty());
    }
    @Test
    void containsTest(){
       Arrays.stream(array).forEach(n -> assertTrue(collection.contains(n)));
       assertFalse(collection.contains(10000000));
    }
    @Test 
    void performanceTest() {
        collection.clear();
        fillBigCollection();
        collection.removeIf(n -> n % 2 == 0);
        assertTrue(collection.stream().allMatch(n -> n % 2 != 0));
        collection.clear();
        assertTrue(collection.isEmpty());
    }
    protected void fillBigCollection() {
        IntStream.range(0, N_ELEMENTS).forEach(i -> collection.add(random.nextInt()));
    }

}