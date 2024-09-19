package telran.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//{3, -10, 20, 1, 10, 8, 100 , 17};
public class TreeSetTest extends SortedSetTest{
    TreeSet<Integer> treeSet;
    @BeforeEach
    @Override
    void setUp() {
        collection = new TreeSet<>();
        super.setUp();
        treeSet = (TreeSet<Integer>)collection;
    }
    
    @Test
    void displayTreeRotatedTest(){
        System.out.println("\n------------Displaying Tree Rotated---------------");
        treeSet.setSymbolsPerLevel(5);
        treeSet.displayTreeRotated();
    }
    @Test
    void displayTreeParentChildrenTest(){
        System.out.println("\n------------Displaying Tree Parent-Children---------------");
        treeSet.setSymbolsPerLevel(3);
        treeSet.displayTreeParentChildren();
    }
    @Test
    void widthtest() {
        assertEquals(4, treeSet.width());
    }
    @Test
    void heightTest() {
        assertEquals(4, treeSet.height());
    }
    @Test
    void inversionTest() {
            Integer[] expected = {100, 20, 17, 10, 8, 3, 1, -10};
            treeSet.inversion();
            Integer[] actual = treeSet.stream().toArray(Integer[]::new);
            assertArrayEquals(expected, actual);
            assertTrue(treeSet.contains(100));
    }
    @Test
    void extremeCasesTest() {
        TreeSet<Integer> tree = new TreeSet<>();
        IntStream.rangeClosed(1, 7).boxed().forEach(tree::add);
        assertEquals(7, tree.height());
        assertEquals(1, tree.width());
        tree.clear();
        Integer[] balancedArray = {4,2,1,3,6,5,7};
        Arrays.stream(balancedArray).forEach(tree::add);
        assertEquals(3, tree.height());
        assertEquals(4, tree.width());

    }
    @Test
    void balanceTestCW() {
        TreeSet<Integer> tree = new TreeSet<>();
        Integer[] array = getBigArrayCW();
        Arrays.stream(array).forEach(tree::add);
        tree.balance();
       assertEquals(20, tree.height());
        assertEquals((N_ELEMENTS + 1) / 2, tree.width());
    }
    @Test
    void balanceTestHW() {
        TreeSet<Integer> tree = new TreeSet<>();
        Integer[] array = getBigArrayHW();
        Arrays.stream(array).forEach(tree::add);
        assertEquals(20, tree.height());
        assertEquals((N_ELEMENTS + 1) / 2, tree.width());
    }

}