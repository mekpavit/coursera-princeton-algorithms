import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private Item[] s;
  private int size = 0;


  public RandomizedQueue() {
    s = (Item[]) new Object[2];
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

  private void resize(int length) {
    Item[] newS = (Item[]) new Object[length];
    for (int i = 0; i < size; i++) {
      newS[i] = s[i];
    }
    s = newS;
  }

  public void enqueue(Item item) {
    validateItem(item);

    if (size == s.length) {
      resize(2 * size);
    }

    s[size] = item;
    size++;

  }

  private void validateReturnItem() {
    if (size == 0) {
      throw new NoSuchElementException();
    }
  }

  public Item dequeue() {
    validateReturnItem();
    int randomSIndex = StdRandom.uniform(0, size);
    Item randomItem = s[randomSIndex];

    if (size > 0 && size == s.length / 4) {
      resize(s.length / 2);
    }

    s[randomSIndex] = s[size - 1];
    s[size - 1] = null;
    size--;
    return randomItem;
  }

  public Item sample() {
    validateReturnItem();
    int randomSIndex = StdRandom.uniform(0, size);
    Item randomItem = s[randomSIndex];
    return randomItem;
  }

  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator();
  }

  private class RandomizedQueueIterator implements Iterator<Item> {

    Item[] randomizedS = (Item[]) new Object[size];
    int currentIndex = 0;

    public RandomizedQueueIterator() {
      Item[] tempS = (Item[]) new Object[size];
      for (int i = 0; i < size; i++) {
        tempS[i] = s[i];
      }
      for (int i = 0; i < size; i++) {
        int randomizedSIndex = StdRandom.uniform(0, size - i);
        randomizedS[i] = tempS[randomizedSIndex];
        tempS[randomizedSIndex] = tempS[size - 1 - i];
        tempS[size - 1 - i] = null;
      }
    }

    public boolean hasNext() {
      return currentIndex != size;
    }

    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Item nextItem = randomizedS[currentIndex];
      currentIndex++;
      return nextItem;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

  public static void main(String[] args) {
    // isEmpty
    StdOut.println("Test Case: isEmpty");
    RandomizedQueue<Integer> rq1 = new RandomizedQueue<Integer>();
    StdOut.printf("%-100s%-1s%n", "isEmpty should return true when RandomizedQueue size = 0", (rq1.isEmpty() == true));
    rq1.enqueue(5);
    StdOut.printf("%-100s%-1s%n", "isEmpty should return true when RandomizedQueue size = 0", (rq1.isEmpty() == false));

    // size
    StdOut.println("Test Case: size");
    RandomizedQueue<Integer> rq2 = new RandomizedQueue<Integer>();
    StdOut.printf("%-100s%-1s%n", "isEmpty should return true when RandomizedQueue size = 0", (rq2.size() == 0));
    rq2.enqueue(5);
    rq2.enqueue(10);
    rq2.enqueue(15);
    StdOut.printf("%-100s%-1s%n", "isEmpty should return true when RandomizedQueue size = 3", (rq2.size() == 3));

    // enqueue
    StdOut.println("Test Case: enqueue");
    RandomizedQueue<Integer> rq3 = new RandomizedQueue<Integer>();
    rq3.enqueue(5);
    rq3.enqueue(10);
    rq3.enqueue(15);
    rq3.enqueue(20);
    rq3.enqueue(25);
    StdOut.printf("%-100s%-1s%n", "enqueue should not throw any error", true);

    // dequeue
    StdOut.println("Test Case: dequeue");
    RandomizedQueue<Integer> rq4 = new RandomizedQueue<Integer>();
    rq4.enqueue(1);
    StdOut.printf("%-100s%-1s%n", "dequeue should return 1", rq4.dequeue() == 1);
    rq4.enqueue(5);
    rq4.enqueue(10);
    rq4.enqueue(15);
    rq4.enqueue(20);
    rq4.enqueue(25);
    StdOut.printf("%-100s%-1s%n", "dequeue should return something", rq4.dequeue() != null);
    StdOut.printf("%-100s%-1s%n", "dequeue should return something", rq4.dequeue() != null);
    StdOut.printf("%-100s%-1s%n", "dequeue should return something", rq4.dequeue() != null);
    StdOut.printf("%-100s%-1s%n", "dequeue should return something", rq4.dequeue() != null);
    StdOut.printf("%-100s%-1s%n", "dequeue should return something", rq4.dequeue() != null);

    // sample
    StdOut.println("Test Case: sample");
    RandomizedQueue<Integer> rq5 = new RandomizedQueue<Integer>();
    rq5.enqueue(5);
    StdOut.printf("%-100s%-1s%n", "sample should return 5", rq5.sample() == 5);
    rq5.enqueue(10);
    rq5.enqueue(15);
    rq5.enqueue(20);
    rq5.enqueue(25);
    StdOut.printf("%-100s%-1s%n", "sample should return something", rq5.sample() != null);
    StdOut.printf("%-100s%-1s%n", "sample should return something", rq5.sample() != null);
    StdOut.printf("%-100s%-1s%n", "sample should return something", rq5.sample() != null);
    StdOut.printf("%-100s%-1s%n", "sample should return something", rq5.sample() != null);
    StdOut.printf("%-100s%-1s%n", "sample should return something", rq5.sample() != null);

    // iterator
    StdOut.println("Test Case: iterator");
    RandomizedQueue<Integer> rq6 = new RandomizedQueue<Integer>();
    rq6.enqueue(5);
    Iterator<Integer> rq6Iterator = rq6.iterator();
    StdOut.printf("%-100s%-1s%n", "hasNext should return true", rq6Iterator.hasNext() == true);
    StdOut.printf("%-100s%-1s%n", "next should return 5", rq6Iterator.next() == 5);
    StdOut.printf("%-100s%-1s%n", "hasNext should return false", rq6Iterator.hasNext() == false);
    rq6.enqueue(10);
    rq6.enqueue(15);
    rq6.enqueue(20);
    rq6.enqueue(25);
    Iterator<Integer> rq6Iterator2 = rq6.iterator();
    StdOut.printf("%-100s%-1s%n", "hasNext should return true", rq6Iterator2.hasNext() == true);
    StdOut.printf("%-100s%-1s%n", "next should return something", rq6Iterator2.next() != null);
    StdOut.printf("%-100s%-1s%n", "hasNext should return true", rq6Iterator2.hasNext() == true);
    StdOut.printf("%-100s%-1s%n", "next should return something", rq6Iterator2.next() != null);
    StdOut.printf("%-100s%-1s%n", "hasNext should return true", rq6Iterator2.hasNext() == true);
    StdOut.printf("%-100s%-1s%n", "next should return something", rq6Iterator2.next() != null);
    StdOut.printf("%-100s%-1s%n", "hasNext should return true", rq6Iterator2.hasNext() == true);
    StdOut.printf("%-100s%-1s%n", "next should return something", rq6Iterator2.next() != null);
    StdOut.printf("%-100s%-1s%n", "hasNext should return true", rq6Iterator2.hasNext() == true);
    StdOut.printf("%-100s%-1s%n", "next should return something", rq6Iterator2.next() != null);
    StdOut.printf("%-100s%-1s%n", "hasNext should return false", rq6Iterator2.hasNext() == false);
  }

}