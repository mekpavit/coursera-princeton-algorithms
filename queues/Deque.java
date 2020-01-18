import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item>{

  private static class Node<E> {
    public E value;
    public Node<E> next;
    public Node<E> before;
  }

  private Node<Item> firstNode = null;
  private Node<Item> lastNode = null;
  private int size = 0;

  public Deque() {
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  private void validateItem(Item item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
  }

  public void addFirst(Item item) {

    validateItem(item);

    Node<Item> node = new Node<Item>();
    node.value = item;
    node.next = firstNode;
    if (size != 0) {
      firstNode.before = node;
    }
    firstNode = node;

    if (size == 0) {
      lastNode = node;
    }

    size++;
  }

  public void addLast(Item item) {

    validateItem(item);

    Node<Item> node = new Node<Item>();
    node.value = item;
    node.before = lastNode;
    if (size != 0) {
      lastNode.next = node;
    }
    lastNode = node;

    if (size == 0) {
      firstNode = node;
    }

    size++;

  }

  private void validateRemove() {
    if (size == 0) {
      throw new java.util.NoSuchElementException();
    }
  }

  public Item removeFirst() {
    validateRemove();
    Item oldFirstNodeItem = firstNode.value;
    firstNode = firstNode.next;
    if (size != 1) {
      firstNode.before = null;
    }
    size--;
    return oldFirstNodeItem;
  }

  public Item removeLast() {
    validateRemove();
    Item oldLastNodeItem = lastNode.value;
    lastNode = lastNode.before;
    if (size != 1) {
      lastNode.next = null;
    }
    size--;
    return oldLastNodeItem;
  }

  public Iterator<Item> iterator() {
    return new DequeIterator();
  }

  private class DequeIterator implements Iterator<Item> {

    private Node<Item> current = firstNode;

    public boolean hasNext() {
      return !(current == null);
    }

    public Item next() {
      Item currentItem = current.value;
      current = current.next;
      return currentItem;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

  public static void main(String[] args) {

    // isEmpty
    Deque<Integer> dq1 = new Deque<Integer>();
    StdOut.printf("%-100s%-1s%n", "isEmpty should return true when Deque is empty:", (dq1.isEmpty() == true));
    dq1.addFirst(5);
    StdOut.printf("%-100s%-1s%n", "isEmpty should return false when Deque is not empty:", (dq1.isEmpty() == false));

    // size
    Deque<Integer> dq2 = new Deque<Integer>();
    StdOut.printf("%-100s%-1s%n", "size should return 0 when Deque is empty:", (dq2.size() == 0));
    dq2.addFirst(6);
    StdOut.printf("%-100s%-1s%n", "size should return 1 when Deque's size is 1:", (dq2.size() == 1));
    dq2.addLast(9);
    StdOut.printf("%-100s%-1s%n", "size should return 2 when Deque's size is 2", (dq2.size() == 2));
    dq2.removeFirst();
    StdOut.printf("%-100s%-1s%n", "size should return 1 when Deque's size is 1", (dq2.size() == 1));

    // removeFirst
    Deque<Integer> dq3 = new Deque<Integer>();
    dq3.addFirst(5);
    dq3.addFirst(10);
    StdOut.printf("%-100s%-1s%n", "first removeFirst should return 10", (dq3.removeFirst() == 10));
    StdOut.printf("%-100s%-1s%n", "second removeFirst should return 5", (dq3.removeFirst() == 5));

    //removeLast
    Deque<Integer> dq4 = new Deque<Integer>();
    dq4.addFirst(20);
    dq4.addFirst(50);
    StdOut.printf("%-100s%-1s%n", "first removeLast should return 20", (dq4.removeLast() == 20));
    StdOut.printf("%-100s%-1s%n", "second removeLast should return 50", (dq4.removeLast() == 50));

    //iterator
    Deque<Integer> dq5 = new Deque<Integer>();
    dq5.addFirst(50);
    dq5.addLast(20);
    dq5.addLast(30);
    dq5.addFirst(10);
    dq5.addFirst(100);
    dq5.removeLast();
    dq5.removeFirst();
    Iterator<Integer> dq5Interator = dq5.iterator();
    StdOut.printf("%-100s%-1s%n", "first item should be 10", (dq5Interator.next() == 10));
    StdOut.printf("%-100s%-1s%n", "after first item hasNext should still be true", (dq5Interator.hasNext() == true));
    StdOut.printf("%-100s%-1s%n", "second item should be 50", (dq5Interator.next() == 50));
    StdOut.printf("%-100s%-1s%n", "after second item hasNext should still be true", (dq5Interator.hasNext() == true));
    StdOut.printf("%-100s%-1s%n", "third item should be 20", (dq5Interator.next() == 20));
    StdOut.printf("%-100s%-1s%n", "after third item hasNext should be false", (dq5Interator.hasNext() == false));
  }

}