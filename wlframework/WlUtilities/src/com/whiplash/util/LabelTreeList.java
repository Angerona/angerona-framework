/*
 * LabelTreeList.java    1.2 2000/01/30
 *
 * Copyright (c) 1999 Stefan Nilsson
 * KTH, Nada
 * SE-100 44 Stockholm, Sweden
 * http://www.nada.kth.se/~snilsson
 *
 * The code presented in this file has been tested with care but
 * is not guaranteed for any purpose. The writer does not offer
 * any warranties nor does he accept any liabilities with respect
 * to the code.
 */

package com.whiplash.util;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.io.Serializable;

/**
 * An efficient implementation of the <tt>java.util.List</tt> interface
 * that implements all optional list operations and permits all elements,
 * including <tt>null</tt>.<p>
 *
 * All methods accesing a single element run in time O(log n), where n
 * is the size of the list. This implementation uses more memory than
 * an <tt>IndexTreeList</tt>. On the other hand all methods, including
 * <tt>contains(Object o)</tt>, <tt>indexOf(Object o )</tt>,
 * <tt>lastIndexOf(Object o)</tt>, and <tt>remove(Object o)</tt>,
 * run in O(log n) time.<p>
 *
 * Like the standard JDK List implementations this data structure
 * is not synchronized.
 * If multiple threads access a <tt>LabelTreeList</tt> object concurrently,
 * and at least one of the threads modifies the list structurally, it
 * must be synchronized externally. (A structural modification is any
 * operation that adds or deletes one or more elements,
 * merely setting the value of an element is not a structural modification.)
 * This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the list.
 * If no such object exists, the list should be "wrapped" using the
 * <tt>Collections.synchronizedList</tt> method. This is best done at
 * creation time, to prevent accidental unsynchronized access to the list:
 *
 * <pre>
 *     List list = Collections.synchronizedList(new LabelTreeList(...));
 * </pre>
 *
 * The iterators returned by this class's <tt>iterator</tt> and
 * <tt>listIterator</tt> methods are fail-fast: if the list is
 * structurally modified at any time after the iterator is created, in any
 * way except through the iterator's own remove or add methods, the iterator
 * will throw a ConcurrentModificationException. In this case no changes will
 * be performed on the underlying list (this behavior may differ from
 * an ArrayList).<p>
 *
 * For a detailed discussion of this data structure, see the article
 * "An efficient list implementation" by A. Andersson and S. Nilsson,
 * 1999, http://www.nada.kth.se/~snilsson.
 *
 * @author  Stefan Nilsson
 * @version 1.2, 30 jan 2000
 * @see            java.util.List
 * @see     IndexTreeList
 */
@SuppressWarnings("all")
public class LabelTreeList<T> extends AbstractList<T> implements List<T>, Cloneable, Serializable{
    
	private static final long serialVersionUID = 1L;

	/**
     * The complete tree is rebalanced when numOfDeletions > DC * size().
     */
    private final static int DC = 8;

    /**
     * Each node in the tree has a label. The number of bits in this
     * label needs to be larger than MAX_HEIGHT. We use long for labels
     * and hence the number of bits is 64. This implies that the
     * maximum height of the tree must not be more than 62 (the sign
     * of the label is not used and an extra bit is used to indicate
     * where the label ends).
     */
    private final static int LABEL_SIZE = 64;

    /**
     * The array MIN_SIZE gives the minimum number of elements allowed
     * in a tree of a certain height before rebalancing takes place.
     * (Should be final, but compiler complains.)
     */
    private static long[] MIN_SIZE;

    /**
     * The size of the array MIN_SIZE is MAX_HEIGHT + 1.
     * (Should be final, but compiler complains.)
     */
    private static int MAX_HEIGHT; // = 43 for C = 1.35

    /** 
     * Using 64 bit labels the parameter C must be in the interval
     * 1.00 to 1.95.
     */
    private final static double C = 1.35;

    static {
        MAX_HEIGHT = 1 + (int) Math.ceil(C * Math.log(Integer.MAX_VALUE) /
                                         Math.log(2));
        MIN_SIZE = new long[MAX_HEIGHT + 1];
        for (int h = 1; h <= MAX_HEIGHT; h++)
            MIN_SIZE[h] = (long) (Math.exp((h - 1) / C * Math.log(2)) + 0.5);
    }

    /**
     * The tree into which the elemens are stored.
     */
    private transient Entry root = null;

    /**
     * A hashtable contaning an entry for each key. A key maps
     * to a splay tree containing all tree entries for this key.
     */
    private transient HashMap hash = new HashMap();

    /**
     * The number of deletions performed since last global rebuilding.
     */
    private transient long numOfDeletions = 0;

    /**
     * Constructs an empty list.
     */
    public LabelTreeList() {
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     */
    public LabelTreeList(Collection c) {
        // Build a completely skew tree.
        Iterator it = c.iterator();
        final int size = c.size();
        Entry r = null;
        for (int i = 1; i <= size; i++) {
            Entry t = new Entry(it.next(), 0); // The labels fixed below.
            t.size = i;
            t.left = r;
            r = t;
        }

        // Balance the tree in linear time and construct the labels.
        if (r != null)
            root = perfectBalance(r, 0);

        // Build the hash table corresponding to the tree.
        if (root != null)
            addTreeToHash(root, hash);
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return  the number of elements in this list.
     */
    public int size() {
        return root == null ? 0 : root.size;
    }

    /**
     * Tests if this list has no elements.
     *
     * @return  <tt>true</tt> if this list has no elements;
     *          <tt>false</tt> otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param  index index of element to return.
     * @return the element at the specified position in this list.
     * @throws    IndexOutOfBoundsException if index is out of range <tt>(index
     *                   &lt; 0 || index &gt;= size())</tt>.
     */
    public T get(int index) {
        rangeCheck(index);
        return (T) getEntry(index).data;
    }

    private Entry getEntry(int index) {
        Entry t = root;
        while (true) {
            Entry left = t.left;
            int rank = left == null ? 0 : left.size;
            if (index == rank)
                return t;
            if (index < rank)
                t = left;
            else {
                index -= rank + 1;
                t = t.right;
            }
        }
    }

    /**
     * Replaces the element at the specified position in this list with
     * the specified element.
     *
     * @param index index of element to replace.
     * @param element element to be stored at the specified position.
     * @return the element previously at the specified position.
     * @throws    IndexOutOfBoundsException if index out of range
     *                  <tt>(index &lt; 0 || index &gt;= size())</tt>.
     */
    public Object set(int index, Object element) {
        rangeCheck(index);

        Entry t = getEntry(index);
        removeFromHash(t, hash);
        Object oldValue = t.data;
        t.data = element;
        addNodeToHash(t, hash);
        return oldValue;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param element element to be appended to this list.
     * @return <tt>true</tt> (as per the general contract of Collection.add).
     */
    public boolean add(Object element) {
        Entry t = root;
        int d = 1; // keep track of depth
        long label = 0; // construct the bit path

        modCount++;
        if (t == null) {
            root = new Entry(element, 1L << LABEL_SIZE - 2);
            addNodeToHash(root, hash);
            return true;
        }

        while (t.right != null) {
            d++;
            label |= 1L << LABEL_SIZE - d;
            t.size++;
            t = t.right;
        }
        label |= 1L << LABEL_SIZE - 1 - d;
        t.size++;
        t.right = new Entry(element, label | 1L << LABEL_SIZE - 2 - d);
        addNodeToHash(t.right, hash);
        if (root.size < MIN_SIZE[d])
            fixBalance(root.size - 1);
        return true;
    }

    /**
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted.
     * @param element element to be inserted.
     * @throws    IndexOutOfBoundsException if index is out of range
     *                  <tt>(index &lt; 0 || index &gt; size())</tt>.
     */
    public void add(int index, Object element) {
        if (index == size()) {
            add(element);
            modCount++;
            return;
        }
        rangeCheck(index);
        modCount++;

        Entry t = root;
        int d = 1; // keep track of depth
        long label = 0; // construct the bit path

        // find node at index
        int i = index;
        while (true) {
            Entry left = t.left;
            t.size++;
            int rank = left == null ? 0 : left.size;
            if (i == rank)
                break;
            d++;
            if (i < rank)
                t = left;
            else {
                i -= rank + 1;
                t = t.right;
                label |= 1L << LABEL_SIZE - d;
            }
        }

        if (t.left == null) {
            t.left = new Entry(element, label |= 1L << LABEL_SIZE - 2 - d);
            addNodeToHash(t.left, hash);
        } else {   // walk left, right, right, ...
            d++;
            t = t.left;
            while (t.right != null) {
                d++;
                t.size++;
                t = t.right;
                label |= 1L << LABEL_SIZE - d;
            }
            t.size++;
            label |= 1L << LABEL_SIZE - 1 - d;
            t.right = new Entry(element, label |= 1L << LABEL_SIZE - 2 - d);
            addNodeToHash(t.right, hash);
        }

        if (root.size < MIN_SIZE[d])
            fixBalance(index);
    }

    /**
     * Removes the first occurrence in this list of the specified element.
     * If this list does not contain the element, it is
     * unchanged.  More formally, removes the element with the lowest index i
     * such that 
     * <tt>(element==null ? get(i)==null : element.equals(get(i)))</tt>
     * (if such an element exists).
     *
     * @param element element to be removed from this list, if present.
     * @return <tt>true</tt> if this list contained the specified element.
     */
     public boolean remove(Object element) {
         int i = indexOf(element);
         if (i != -1) {
             remove(i);
             return true;
         } else
             return false;
     }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index the index of the element to removed.
     * @return the element that was removed from the list.
     * @throws    IndexOutOfBoundsException if index out of range <tt>(index
     *                   &lt; 0 || index &gt;= size())</tt>.
     */
    public T remove(int index) {
        rangeCheck(index);
        modCount++;
        numOfDeletions++;

        // Find the element
        Entry t = root;
        Entry tParent = null;
        while (true) {
            Entry left = t.left;
            int rank = left == null ? 0 : left.size;
            if (index == rank)
                break;
            t.size--;
            tParent = t;
            if (index < rank)
                t = left;
            else {
                index -= rank + 1;
                t = t.right;
            }
        }
        Object oldValue = t.data;

        removeFromHash(t, hash);
        if (tParent == null)
            root = deleteRoot(root);
        else if (t == tParent.left)
            tParent.left = deleteRoot(t);
        else
            tParent.right = deleteRoot(t);

        final int size = size();
        if (size < 3) {
            numOfDeletions = 0;
        } else if (numOfDeletions > DC * (long) size) {
            root = perfectBalance(root, 0);
            numOfDeletions = 0;
        }
        return (T) oldValue;
    }

    /**
     * Removes from this list all of the elements whose index is between
     * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.
     * Shifts any succeeding elements to the left (reduces their index).
     * This call shortens the ArrayList by <tt>(toIndex - fromIndex)</tt>
     * elements. (If <tt>toIndex==fromIndex</tt>, this operation has no
     * effect.)
     *
     * @param fromIndex index of first element to be removed.
     * @param toIndex index after last element to be removed.
     */
    protected void removeRange(int fromIndex, int toIndex) {
        for (int i = toIndex - fromIndex; i > 0; i--)
            remove(fromIndex);
    }

    /**
     * Removes all of the elements from this list.  The list will
     * be empty after this call returns.
     */
    public void clear() {
        modCount++;
        numOfDeletions = 0;
        root = null;
        hash.clear();
    }

    /**
     * Returns <tt>true</tt> if this collection contains the specified
     * element.  More formally, returns <tt>true</tt> if and only if this
     * collection contains at least one element <tt>e</tt> such that
     * <tt>(element==null ? e==null : element.equals(e))</tt>.<p>
     *
     * This implementation iterates over the elements in the collection,
     * checking each element in turn for equality with the specified element.
     *
     * @param element object to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains the specified element.
     */
    public boolean contains(Object element) {
        return hash.containsKey(element);
    }

    /**
     * Returns the index in this list of the first occurence of the
     * specified element, or -1 if this list does not contain this
     * element.
     *
     * @param element element to search for
     * @return the index in this list of the first occurence of the
     *         specified element, or -1 if this list does not contain
     *         this element.
     */
    public int indexOf(Object element) {
        if (!(hash.containsKey(element)))
            return -1;
        Object hashEntry = hash.get(element);
        if (hashEntry instanceof Entry)
            return getIndex((Entry) hashEntry);
        else {
            SplayTree t = (SplayTree) hashEntry;
            t = t.moveMinToRoot();
            hash.put(element, t);
            return getIndex(t.data);
        }
    }

    /**
     * Returns the index in this list of the last occurence of the
     * specified element, or -1 if this list does not contain this
     * element.
     *
     * @param element element to search for
     * @return the index in this list of the last occurence of the
     *         specified element, or -1 if this list does not contain
     *         this element.
     */
    public int lastIndexOf(Object element) {
        if (!(hash.containsKey(element)))
            return -1;
        Object hashEntry = hash.get(element);
        if (hashEntry instanceof Entry) {
            return getIndex((Entry) hashEntry);
        } else {
            SplayTree t = (SplayTree) hashEntry;
            t = t.moveMaxToRoot();
            hash.put(element, t);
            return getIndex(t.data);
        }
    }

    private int getIndex(Entry e) {
        Entry t = root;
        int d = 0;
        long label = e.label;
        int index = 0;

        while (true) {
            Entry left = t.left;
            int rank = left == null ? 0 : left.size;
            if (t == e)
                return index + rank;
            d++;
            if ((label & 1L << LABEL_SIZE - 1 - d) == 0) {
                t = left;
            } else {
                index += rank + 1;
                t = t.right;
            }
        }
    }

    /**
     * Returns a shallow copy of this <tt>LabelTreeList</tt> instance.
     * (The elements themselves are not copied.)
     *
     * @return a clone of this <tt>LabelTreeList</tt> instance.
     */
    public Object clone() {
        try {
            LabelTreeList v = (LabelTreeList) super.clone();
            if (root != null)
                v.root = (Entry) root.clone();

            // The new structure has never been modified.
            v.modCount = 0;

            // Build a new hash table corresponding to the tree.
            v.hash = new HashMap();
            if (root != null)
                addTreeToHash(v.root, v.hash);

            return v;
        } catch (CloneNotSupportedException e) {
            // This cannot happen
            throw new InternalError();
        }
    }

    /**
     * Save the state of the <tt>LabelTreeList</tt> instance to a stream
     * (that is, serialize it).
     *
     * @serialData The size of the list (int) followed by all the list elements
     *             (each an <tt>Object</tt>) in the proper order.
     */
    private synchronized void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        // any hidden stuff
        s.defaultWriteObject();

        // the size of the list
        s.writeInt(size());

        // all elements in the proper order
        if (root != null)
            writeTree(s, root);
    }

    private void writeTree(java.io.ObjectOutputStream s, Entry t)
        throws java.io.IOException {
        if (t.left != null)
            writeTree(s, t.left);
        s.writeObject(t.data);
        if (t.right != null)
            writeTree(s, t.right);
    }

    /**
     * Reconstitute the <tt>LabelTreeList</tt> instance from a stream
     * (that is, deserialize it).
     */
    private synchronized void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        // any hidden stuff
        s.defaultReadObject();

        // the size of the list
        int size = s.readInt();

        // Build a completely skew tree.
        Entry r = null;
        for (int i = 1; i <= size; i++) {
            Entry t = new Entry(s.readObject(), 0); // The labels fixed below.
            t.size = i;
            t.left = r;
            r = t;
        }
        // Balance the tree in linear time and construct the labels.
        if (r != null)
            root = perfectBalance(r, 0);

        // Build the hash table corresponding to the tree.
        hash = new HashMap();
        if (root != null)
            addTreeToHash(root, hash);
    }

    /**
     * Check if the given index is in range.  If not, throw an appropriate
     * runtime exception.
     */
    private void rangeCheck(int index) {
        if (index >= size() || index < 0)
            throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size());
    }


    /**
     * TEST: Print the complete data structure to System.out.
     */
    public void testPrint() {
        System.out.println("modCount: " + modCount);
        System.out.println("numOfDeletions: " + numOfDeletions);
        System.out.println("tree:");
        testPrintTree(root, 0);
        System.out.println();
        System.out.println("hash: " + hash);
    }

    /**
     * TEST
     */
    private void testPrintTree(Entry t, int depth) {
        if (t == null)
            return;
        testPrintTree(t.left, depth + 1);
        // Indent the complete tree.
        System.out.print("     ");
        // Indent this node.
        for (int i = 0; i < depth; i ++)
            System.out.print("   ");
        System.out.println(t.toLongString());
        testPrintTree(t.right, depth + 1);
    }

    /**
     * A node in the labelled index tree.
     */
    private static class Entry implements Cloneable {
        Object data;
        Entry left = null;
        Entry right = null;
        int size = 1;  // Size of the tree rooted at this node
        /**
         * The label indicates the path from the root th this node.
         * Bit number 0 (the most significant) is not used.
         * 0 indicates a left turn, 1 a right turn. The string
         * "10000..." is used as an end marker. For example:
         * The label "0101000..." indicates that this node is
         * the left child of the right child of the root in the tree.
         */
        long label;

        Entry(Object data, long label) { 
            this.data = data;
            this.label = label;
        }

        public String toString() {
            return data == null ? "null" : data.toString();
        }

        public String toLongString() {
            StringBuffer buf = new StringBuffer();
            buf.append("data=" + toString());
            buf.append(", size=" + size);
            buf.append(", label=(");
            buf.append(label >>> LABEL_SIZE - 1 & 1);
            buf.append(")");
            for (int i = 1; i < 16; i++) {
                if (i%8 == 0 && i != 0)
                    buf.append(" ");
                buf.append(label >>> LABEL_SIZE - 1 - i & 1);
            }
            buf.append("...");
            return buf.toString();
        }

        /**
         * Return a deep copy the complete tree structure.
         */
        public Object clone() {
            try {
                Entry t = (Entry) super.clone();
                if (left != null)
                    t.left = (Entry) left.clone();
                if (right != null)
                    t.right = (Entry) right.clone();
                return t;
            } catch (CloneNotSupportedException e) {
                // Cannot happen
                throw new InternalError();
            }
        }
    }

    /**
     * Add the entry to the hash table.
     */
    private void addNodeToHash(Entry e, HashMap hash) {
        Object key = e.data;
        if (!hash.containsKey(key))
            hash.put(key, e);
        else {
            Object hashEntry = hash.get(key);
            SplayTree t;
            if (hashEntry instanceof Entry)
                t = new SplayTree((Entry) hashEntry, null, null);
            else
                t = (SplayTree) hash.get(key);
            t = t.insert(e);
            hash.put(key, t);
        }
    }

    /**
     * Add all nodes in this tree to a hashtable h.
     * Root must not be null.
     */
    private void addTreeToHash(Entry root, HashMap h) {
        addNodeToHash(root, h);
        if (root.left != null)
            addTreeToHash(root.left, h);
        if (root.right != null)
            addTreeToHash(root.right, h);
    }


    /**
     * Remove this entry from the hash table.
     */
    private void removeFromHash(Entry e, HashMap hash) {
        Object key = e.data;
        Object hashEntry = hash.get(key);
        if (hashEntry instanceof Entry)
            hash.remove(key);
        else {
            SplayTree t = (SplayTree) hashEntry;
            t = t.delete(e);
            if (t == null)
                hash.remove(key);
            else
                hash.put(key, t);
        }
    }


    // Balancing operations.

    private Entry rotateLeft(Entry t) {
        // Do the rotation.
        final Entry tmp = t;
        t = tmp.right;
        tmp.right = t.left;
        t.left = tmp;

        // Fix the sizes.
        final Entry left = t.left;
        final int temp = left.size - t.size;
        t.size = left.size;
        left.size = temp + (left.right == null ? 0 : left.right.size);

        // The labels are fixed after all rotations are done.

        return t;
    }

    private Entry rotateRight(Entry t) {
        // Do the rotation.
        final Entry tmp = t;
        t = tmp.left;
        tmp.left = t.right;
        t.right = tmp;

        // Fix the sizes.
        final Entry right = t.right;
        final int temp = right.size - t.size;
        t.size = right.size;
        right.size = temp + (right.left == null ? 0 : right.left.size);

        // The labels are fixed after all rotations are done.

        return t;
    }

    /**
     * Produce a right-skewed tree by sliding along the rightmost path,
     * making repeated right rotations.
     */
    private Entry skew(Entry t) {
        Entry s = null;   // The parent of t
        Entry root = t;   // The node to be returned

        do {
            while (t.left != null)
                t = rotateRight(t);
            if (s != null)
                s.right = t;
            else
               root = t;
            s = t;
            t = t.right;
        } while (t != null);
        return root;
    }

    /**
     * Compress a skewed path of p1 nodes into a path of p2 nodes
     * (precondition: 2*p2 >= p1). In order to do this, we traverse
     * the rightmost path making left rotations. At each left
     * rotation, the path length is decreased by 1. Hence, we
     * should make p1-p2 evenly distributed left rotations. To get
     * the rotations evely spaced we use a counter to step from
     * p1-p2 to p2*(p1-p2) with increment equal to p1-p2. Every
     * time this counter reaches or exceeds a multiple of p2 a
     * rotation is performed.
     */
    private Entry split(Entry t, int p1, int p2) {
	int incr = p1 - p2;
	int count = 0;
	Entry s = null; // The parent of t
	Entry root = t; // The node to be returned

	for (int i = p2; i > 0; i--) {
	    count += incr;
	    if (count >= p2) {
		t = rotateLeft(t);
		count -= p2; // incr <= p2
	    }
	    if (s != null)
		s.right = t;
	    else
		root = t;
	    s = t;
	    t = t.right;
	}
	return root;
    }

    /**
     * A simple method for rebalancing a binary search tree. In fact,
     * it is simpler than the Stout-Warren algorithm, the main improvement
     * being the method split() above. First, we skew the tree, then we
     * split until the tree is balanced.
     */
    private Entry perfectBalance(Entry t, int d) {
        int b = 1;
        int size = t.size;
        // The label with end marker removed
        long prefix = t.label & ~0L << LABEL_SIZE - 1 - d;

        t = skew(t);
        while (b <= size + 1)
            b *= 2;
        b /= 2;
        if (b != size + 1)
            t = split(t, size, b - 1);
        while (b > 2) {
            t = split(t, b - 1, b/2 - 1);
            b /= 2;
        }
        redoLabels(t, d, prefix);
        return t;
    }

    /**
     * Reduce the length of a path in the tree.
     *
     * Traverse the path down to node with index and put all nodes in
     * a stack. The path from this node is traversed bottom-up
     * and the weight of the subtrees are counted, until a subtree is
     * found, which in itself does not satisfy the balance criterion.
     * This subtree is rebuilt to perfect balance.
     */
    private void fixBalance(int index) {
        Entry t = root;

        Entry[] stack = new Entry[MAX_HEIGHT + 1];
        int top = -1;
        while (true) {
            Entry left = t.left;
            stack[++top] = t;
            int rank = left == null ? 0 : left.size;
            if (index == rank)
                break;
            if (index < rank)
                t = left;
            else {
                index -= rank + 1;
                t = t.right;
            }
        }

        top--; // We never rebuild a tree of size 1.
        int path = 0;    // Length of path.
        //System.out.println("depth=" + s.size());
        do {
            t = stack[top--];
            path++;
            //System.out.println("size=" + t.size + " path=" + path);
        }  while (t.size >= MIN_SIZE[path]);

        if (t == root)
            root = perfectBalance(root, 0);
        else {
            Entry parent = stack[top];
            if (parent.left == t)
                parent.left = perfectBalance(t, top + 1);
            else
                parent.right = perfectBalance(t, top + 1);
        }
    }

    /**
     *  Delete the root of the tree and return a pointer to
     *  the updated tree.
     */
    private Entry deleteRoot(Entry r) {
        Entry t, tParent;

        if (r.left != null) {
            r.size--;
            t = r.left;
            tParent = r;
            while (t.right != null) {
                t.size--;
                tParent = t;
                t = t.right;
            }
            swap(r, t, tParent);
            if (tParent == r)
                t.left = deleteRoot(r);
            else
                tParent.right = deleteRoot(r);
            return t;
        } else if (r.right != null) {
            r.size--;
            t = r.right;
            tParent = r;
            while (t.left != null) {
                t.size--;
                tParent = t;
                t = t.left;
            }
            swap(r, t, tParent);
            if (tParent == r)
                t.right = deleteRoot(r);
            else
                tParent.left = deleteRoot(r);
            return t;
        } else
            return null;
    }

    private void redoLabels(Entry t, int d, long prefix) {
        t.label = prefix | 1L << LABEL_SIZE - 2 - d;
        if (t.left != null)
            redoLabels(t.left, d + 1, prefix);
        if (t.right != null)
            redoLabels(t.right, d + 1, prefix | 1L << LABEL_SIZE - 2 - d);
    }

    /**
     * Change positions of two nodes in the tree.
     * s must be above t in the tree and tParent must be
     * the parent of t.
     */
    private void swap(Entry s, Entry t, Entry tParent) {
        // First swap the left and right pointers within the nodes.
        // Handle the cases when s is a parent of t separately.
        if (s.left == t) {
            s.left = t.left;
            t.left = s;

            Entry tmp = s.right;
            s.right = t.right;
            t.right = tmp;
        } else if (s.right == t) {
            Entry tmp = s.left;
            s.left = t.left;
            t.left = tmp;

            s.right = t.right;
            t.right = s;
        } else {
            Entry tmp = s.left;
            s.left = t.left;
            t.left = tmp;

            tmp = s.right;
            s.right = t.right;
            t.right = tmp;
        }

        // Fix child pointer in tParent.
        if (tParent != s)
            if (tParent.left == t)
                tParent.left = s;
            else
                tParent.right = s;

        // Swap sizes.
        int tmp = s.size;
        s.size = t.size;
        t.size = tmp;

        // Swap labels
        long bar = s.label;
        s.label = t.label;
        t.label = bar;
    }

    private static class SplayTree {
        Entry data;
        SplayTree left = null;
        SplayTree right = null;

        SplayTree(Entry data, SplayTree left, SplayTree right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        /**
         * Move the node containing data to the root
         * using zig-zig and zig-zag rotations.
         * Precondition: The node is in the tree and it is unique.
         */
        SplayTree moveToRoot(Entry data) {
            if (data == this.data)
                return this;
            if (data.label < this.data.label) {
                if (data == left.data)
                    return rotateRight();
                if (data.label < this.left.data.label) {
                    left.left = left.left.moveToRoot(data);
                    return rotateRight().rotateRight();
                }
                left.right = left.right.moveToRoot(data);
                left = left.rotateLeft();
                return rotateRight();
            } else {
                if (data == right.data)
                    return rotateLeft();
                if (right.data.label < data.label) {
                    right.right = right.right.moveToRoot(data);
                    return rotateLeft().rotateLeft();
                }
                right.left = right.left.moveToRoot(data);
                right = right.rotateRight();
                return rotateLeft();
            }
        }

        /**
         * Insert a new node contaning data into the tree.
         * The new node is splayed to the t of the tree.
         */
        SplayTree insert(Entry data) {
            if (data.label < this.data.label) {
                if (left == null)
                    return new SplayTree(data, null, this);
                if (data.label < left.data.label) {
                    if (left.left == null)
                        left.left = new SplayTree(data, null, null);
                    else
                        left.left = left.left.insert(data);
                    return rotateRight().rotateRight();
                }
                if (left.right == null)
                    left.right = new SplayTree(data, null, null);
                else
                    left.right = left.right.insert(data);
                left = left.rotateLeft();
                return rotateRight();
            } else {
                if (right == null)
                    return new SplayTree(data, this, null);
                if (right.data.label < data.label) {
                    if (right.right == null)
                        right.right = new SplayTree(data, null, null);
                    else
                        right.right = right.right.insert(data);
                    return rotateLeft().rotateLeft();
                }
                if (right.left == null)
                    right.left = new SplayTree(data, null, null);
                else
                    right.left = right.left.insert(data);
                right = right.rotateRight();
                return rotateLeft();
            }
        }

        /**
         * Delete the node contaning data from the tree.
         * The node exists and is uniquely determined.
         */
        SplayTree delete(Entry data) {
            SplayTree t = moveToRoot(data);

            if (t.right != null)
                t.right = t.right.moveMinToRoot();

            // Remove root and join the subtrees.
            if (t.right == null)
                return t.left;
            else {
                t.right.left = t.left;
                return t.right;
            }
        }

        SplayTree moveMinToRoot() {
            SplayTree t = this;
            while (t.left != null)
                t = t.left;
            return moveToRoot(t.data);
        }

        SplayTree moveMaxToRoot() {
            SplayTree t = this;
            while (t.right != null)
               t = t.right;
            return moveToRoot(t.data);
        }

        SplayTree rotateLeft() {
            SplayTree t = right;
            right = t.left;
            t.left = this;
            return t;
        }

        SplayTree rotateRight() {
            SplayTree t = left;
            left = t.right;
            t.right = this;
            return t;
        }

        void printTree(int depth) {
            if (left != null)
                left.printTree(depth + 1);
            for (int i = 0; i < depth; i ++)
                System.out.print("   ");
            System.out.println(data);
            if (right != null)
                right.printTree(depth + 1);
        }

        void printList(StringBuffer b) {
            if (left != null)
                left.printList(b);
            b.append(data);
            b.append(", ");
            if (right != null) {
                right.printList(b);
            }
        }

        public String toString() {
            StringBuffer b = new StringBuffer();
            b.append("[");
            printList(b);
            // Remove last ", "
            if (b.length() > 1)
                b.setLength(b.length() - 2);
            b.append("]");
            return b.toString();
        }        
    }

    /**
     * Returns an iterator over the elements in this list in proper
     * sequence. <p>
     *
     * @return an iterator over the elements in this list in proper sequence.
     * 
     * @see #modCount
     */
    public Iterator iterator() {
        return new Itr();
    }

    /**
     * Returns an iterator of the elements in this list (in proper sequence).
     * This implementation returns <tt>listIterator(0)</tt>.
     * 
     * @return an iterator of the elements in this list (in proper sequence).
     * 
     * @see #listIterator(int)
     */
    public ListIterator listIterator() {
        return listIterator(0);
    }

    /**
     * Returns a list iterator of the elements in this list (in proper
     * sequence), starting at the specified position in the list.  The
     * specified index indicates the first element that would be returned by
     * an initial call to the <tt>next</tt> method.  An initial call to
     * the <tt>previous</tt> method would return the element with the
     * specified index minus one.<p>
     *
     * @param index index of the first element to be returned from the list
     *                    iterator (by a call to the <tt>next</tt> method).
     * 
     * @return a list iterator of the elements in this list (in proper
     *                sequence), starting at the specified position in the list.
     * 
     * @throws IndexOutOfBoundsException if the specified index is out of
     *                  range (<tt>index &lt; 0 || index &gt; size()</tt>).
     * 
     * @see #modCount
     */
    public ListIterator listIterator(final int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException("Index: "+index);
        return new ListItr(index);
    }

    /**
     * This shouldn't be necessary but inner classes cannot access protected
     * outer fields from other packages due to a compiler bug.
     */
    private int modCount() {
        return modCount;
    }

    private class Itr implements Iterator {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;

        /**
         * Index of element returned by most recent call to next of
         * previous. Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;

        /**
         * Stack to keep track of current position in the tree.
         */
        Entry[] stack = new Entry[MAX_HEIGHT];
        int top;

        /**
         * The modCount value that the iterator believes that the backing
         * list should have. If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        int expectedModCount = modCount();

        public Itr() {
            setStack(0);
        }

        /**
         * Set up the stack to point to node at index. 0 <= index <= size().
         * If index equals size the stack should point to index - 1.
         */
        final void setStack(int index) {
            top = -1;
            final int size = size();
            if (size == 0) // stack should be empty
                return;

            if (index == size) // cursor in first position after list
                index--;

            Entry t = root;
            while (true) {
                Entry left = t.left;
                stack[++top] = t;
                int rank = left == null ? 0 : left.size;
                if (index == rank) {
                    break;
                } if (index < rank) {
                    t = left;
                } else {
                    index -= rank + 1;
                    t = t.right;
                }
            }
        }

        public boolean hasNext() {
            return cursor < size();
        }

        public Object next() {
            checkForComodification();
            final int size = size();
            if (cursor == size)
                throw new NoSuchElementException();
            lastRet = cursor++;

            Entry node = stack[top];
            // We're at the end of the list and shouldn't step forward.
            if (cursor == size)
                return node.data;

            if (node.right == null) {
                Entry t = stack[top--];
                while (top >= 0 && stack[top].right == t)
                    t = stack[top--];
            } else {
                for (Entry t = node.right; t != null; t = t.left)
                    stack[++top] = t;
            }
            return node.data;
        }

        public void remove() {
            checkForComodification();
            if (lastRet == -1)
                throw new IllegalStateException();

            LabelTreeList.this.remove(lastRet);

            // The cursor shouldn't change if this was
            // preceeded by a call to previous()
            if (lastRet < cursor)
                cursor--;
            lastRet = -1;
            setStack(cursor);
            expectedModCount = modCount();
        }

        final void checkForComodification() {
            if (modCount() != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private class ListItr extends Itr implements ListIterator {

        public ListItr(int index) {
            if (index == 0) // default constructor did the job
                return;

            cursor = index;
            setStack(index);
        }

        public boolean hasPrevious() {
            return cursor > 0;
        }

        public Object previous() {
            checkForComodification();
            if (cursor == 0)
                throw new NoSuchElementException();
            lastRet = --cursor;

            Entry node = stack[top];
            // The stack already points to the last element of the list
            // since we're arriving from the right.
            if (cursor == size() - 1)
                return node.data;

            if (node.left == null) {
                Entry t = stack[top--];
                while (top >= 0 && stack[top].left == t)
                    t = stack[top--];
            } else {
                for (Entry t = node.left; t != null; t = t.right)
                    stack[++top] = t;
            }
            return stack[top].data;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public void set(Object element) {
            checkForComodification();
            if (lastRet == -1)
                throw new IllegalStateException();
            LabelTreeList.this.set(lastRet, element);
        }

        public void add(Object element) {
            checkForComodification();
            LabelTreeList.this.add(cursor++, element);
            lastRet = -1;
            setStack(cursor);
            expectedModCount = modCount();
        }
    }
}
