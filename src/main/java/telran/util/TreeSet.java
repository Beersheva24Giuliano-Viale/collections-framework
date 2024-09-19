package telran.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class TreeSet<T> implements SortedSet<T> {
    private static class Node<T> {
        T obj;
        Node<T> parent;
        Node<T> left;
        Node<T> right;

        Node(T obj) {
            this.obj = obj;
        }
    }

    private class TreeSetIterator implements Iterator<T> {
        Node<T> current = getLeastFrom(root);
        Node<T> prev;

        @Override
        public boolean hasNext() {

            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            prev = current;
            current = getNextCurrent(current);
            return prev.obj;
        }

        @Override
        public void remove() {
            if (prev == null) {
                throw new IllegalStateException();
            }
            removeNode(prev);
            prev = null;
        }

    }

    private Node<T> root;
    private Comparator<T> comparator;
    int size;
    private String printingSymbol = " ";
    private int symbolsPerLevel = 2;

    public TreeSet(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public TreeSet() {
        this((Comparator<T>) Comparator.naturalOrder());
    }

    public void setPrintingSymbol(String printingSymbol) {
        this.printingSymbol = printingSymbol;
    }

    public void setSymbolsPerLevel(int symbolsPerLevel) {
        this.symbolsPerLevel = symbolsPerLevel;
    }

    @Override
    public boolean add(T obj) {
        boolean res = false;
        if (!contains(obj)) {
            res = true;
            Node<T> node = new Node<>(obj);
            addNode(node);

        }
        return res;
    }

    private void addNode(Node<T> node) {
        if (root == null) {
            addRoot(node);
        } else {
            addAfterParent(node);
        }
        size++;
    }

    private void addAfterParent(Node<T> node) {
        Node<T> parent = getParent(node.obj);
        if (comparator.compare(node.obj, parent.obj) > 0) {
            parent.right = node;
        } else {
            parent.left = node;
        }
        node.parent = parent;
    }

    private void addRoot(Node<T> node) {
        root = node;
    }

    @Override
    public boolean remove(T pattern) {
        boolean res = false;
        Node<T> node = getNode(pattern);
        if (node != null) {
            removeNode(node);
            res = true;
        }
        return res;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(T pattern) {
        return getNode(pattern) != null;
    }

    @Override
    public Iterator<T> iterator() {
        return new TreeSetIterator();
    }

    @Override
    public T get(Object pattern) {
        Node<T> node = getNode((T) pattern);

        return node == null ? null : node.obj;
    }

    private Node<T> getParentOrNode(T pattern) {
        Node<T> current = root;
        Node<T> parent = null;
        int compRes = 0;
        while (current != null && (compRes = comparator.compare(pattern, current.obj)) != 0) {
            parent = current;
            current = compRes > 0 ? current.right : current.left;
        }
        return current == null ? parent : current;
    }

    private Node<T> getNode(T pattern) {
        Node<T> res = getParentOrNode(pattern);
        if (res != null) {
            int compRes = comparator.compare(pattern, res.obj);
            res = compRes == 0 ? res : null;
        }
        return res;

    }

    private Node<T> getParent(T pattern) {
        Node<T> res = getParentOrNode(pattern);
        int compRes = comparator.compare(pattern, res.obj);
        return compRes == 0 ? null : res;

    }

    private Node<T> getLeastFrom(Node<T> node) {
        if (node != null) {

            while (node.left != null) {
                node = node.left;
            }
        }
        return node;
    }

    private Node<T> getGreatestFrom(Node<T> node) {
        if (node != null) {

            while (node.right != null) {
                node = node.right;
            }
        }
        return node;
    }

    private Node<T> getGreaterParent(Node<T> node) {
        Node<T> parent = node.parent;
        while (parent != null && parent.right == node) {
            node = node.parent;
            parent = node.parent;
        }
        return parent;
    }

    private Node<T> getNextCurrent(Node<T> current) {
        // Algorithm see on the board
        return current.right != null ? getLeastFrom(current.right) : getGreaterParent(current);
    }

    private void removeNode(Node<T> node) {
        if (node.left != null && node.right != null) {
            removeJunction(node);
        } else {
            removeNonJunction(node);
        }

        size--;
    }

    private void removeJunction(Node<T> node) {
        Node<T> substitute = getGreatestFrom(node.left);
        node.obj = substitute.obj;
        removeNonJunction(substitute);

    }

    private void removeNonJunction(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> child = node.left != null ? node.left : node.right;
        if (parent == null) {
            root = child; // actual root removing
        } else if (node == parent.left) {
            parent.left = child;
        } else {
            parent.right = child;
        }
        if (child != null) {
            child.parent = parent;
        }
        setNulls(node);

    }

    private void setNulls(Node<T> node) {
        node.obj = null;
        node.parent = node.left = node.right = null;

    }

    @Override
    public T first() {
        if (root == null) {
            throw new NoSuchElementException();

        }
        return getLeastFrom(root).obj;
    }

    @Override
    public T last() {
        if (root == null) {
            throw new NoSuchElementException();

        }
        return getGreatestFrom(root).obj;
    }

    @Override
    public T floor(T key) {
        return floorCeilingObj(key, true);
    }

    @Override
    public T ceiling(T key) {
        return floorCeilingObj(key, false);
    }

    @Override
    public SortedSet<T> subSet(T keyFrom, T keyTo) {
        if (comparator.compare(keyFrom, keyTo) > 0) {
            throw new IllegalArgumentException();
        }
        TreeSet<T> subTree = new TreeSet<>(comparator);
        Node<T> ceilingNode = floorCeilingNode(keyFrom, false);
        Node<T> current = ceilingNode;
        while (current != null && comparator.compare(current.obj, keyTo) < 0) {
            subTree.add(current.obj);
            current = getNextCurrent(current);
        }
        return subTree;
    }

    private Node<T> floorCeilingNode(T key, boolean isFloor) {
        Node<T> res = null;
        int compRes = 0;
        Node<T> current = root;
        while (current != null && (compRes = comparator.compare(key, current.obj)) != 0) {
            if ((compRes < 0 && !isFloor) || (compRes > 0 && isFloor)) {
                res = current;
            }
            current = compRes < 0 ? current.left : current.right;
        }
        return current == null ? res : current;

    }

    private T floorCeilingObj(T key, boolean isFloor) {
        T res = null;
        Node<T> node = floorCeilingNode(key, isFloor);
        if (node != null) {
            res = node.obj;
        }
        return res;
    }

    public void displayTreeRotated() {
        displayTreeRotated(root, 0);
    }

    public void displayTreeParentChildren() {
        displayTreeParentChildren(root, 1);
    }

    private void displayTreeParentChildren(Node<T> root, int level) {
        if(root != null) {
            displayRootObject(root.obj, level);
            displayTreeParentChildren(root.left, level + 1);
            displayTreeParentChildren(root.right, level + 1);
        }
    }

    public int width() {
        return width(root);
    }

    private int width(Node<T> root) {
        int res = 0;
        if (root != null) {
            res = root.left == null && root.right == null ? 1 : width(root.left) + width(root.right);
        }
        return res;
    }

    public int height() {
       return height(root);
    }

    private int height(Node<T> root) {
        int res = 0;
        if (root != null) {
            int heightLeft = height(root.left);
            int heightRight = height(root.right);
            res = 1 + Math.max(heightLeft, heightRight);
        }
        return res;
    }

    public void inversion() {
        inversion(root);
        comparator = comparator.reversed();
    }

    private void inversion(Node<T> root) {
       if(root != null) {
         swapLeftRight(root);
         inversion(root.left);
         inversion(root.right);
       }
    }

    private void swapLeftRight(Node<T> root) {
        Node<T> tmp = root.left;
         root.left = root.right;
         root.right = tmp;
    }

    private void displayTreeRotated(Node<T> root, int level) {
        if (root != null) {
            displayTreeRotated(root.right, level + 1);
            displayRootObject(root.obj, level);
            displayTreeRotated(root.left, level + 1);
        }
    }

    private void displayRootObject(T obj, int level) {
        System.out.printf("%s%s\n", printingSymbol.repeat(level * symbolsPerLevel), obj);
    }
    public void balance() {
        Node<T> [] nodes = getSortedNodesArray();
        root = balanceArray(nodes, 0, nodes.length - 1, null);
    }

    private Node<T> balanceArray(Node<T>[] array, int left, int right, Node<T> parent) {
        Node<T> root = null;
       if(left <= right) {
            int middle = (left + right) / 2;
            root = array[middle];
            root.parent = parent;
            root.left = balanceArray(array, left, middle - 1, root);
            root.right = balanceArray(array, middle + 1, right, root);
       }
       return root;
    }

    private Node<T>[] getSortedNodesArray() {
       Node<T>[] array = new Node[size];
        Node<T> current = getLeastFrom(root);
        for(int i = 0; i < size; i++) {
            array[i] = current;
            current = getNextCurrent(current);
        }
        return array;
    }
}