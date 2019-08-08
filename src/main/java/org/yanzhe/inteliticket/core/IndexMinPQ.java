package org.yanzhe.inteliticket.core;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

public class IndexMinPQ<K extends Comparable<K>> implements Iterable<Integer> {

  private int maxN; // maximum number of elements on PQ
  private int n; // number of elements on PQ
  private int[] pq; // binary heap using 1-based indexing
  private int[] qp; // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
  private K[] keys; // keys[i] = priority of i

  public IndexMinPQ(int maxN) {
    if (maxN < 0) {
      throw new IllegalArgumentException();
    }
    this.maxN = maxN;
    n = 0;
    keys = (K[]) new Comparable[maxN + 1]; // make this of length maxN??
    pq = new int[maxN + 1];
    qp = new int[maxN + 1]; // make this of length maxN??
    for (int i = 0; i <= maxN; i++) {
      qp[i] = -1;
    }
  }

  public static void main(String[] args) {
    // insert a bunch of strings
    String[] strings = {"it", "was", "the", "best", "of", "times", "it", "was", "the", "worst"};

    IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
    for (int i = 0; i < strings.length; i++) {
      pq.insert(i, strings[i]);
    }

    // delete and print each key
    while (!pq.isEmpty()) {
      int i = pq.delMin();
      System.out.println(i + " " + strings[i]);
    }
    System.out.println();

    // reinsert the same strings
    for (int i = 0; i < strings.length; i++) {
      pq.insert(i, strings[i]);
    }

    // print each key using the iterator
    for (int i : pq) {
      System.out.println(i + " " + strings[i]);
    }
    while (!pq.isEmpty()) {
      pq.delMin();
    }
  }

  public void clear() {
    while (n != 0) {
      delMin();
    }
  }

  public boolean isEmpty() {
    return n == 0;
  }

  public boolean contains(int i) {
    if (i < 0 || i >= maxN) {
      throw new IllegalArgumentException();
    }
    return qp[i] != -1;
  }

  public int size() {
    return n;
  }

  public void insert(int i, K k) {
    if (i < 0 || i >= maxN) {
      throw new IllegalArgumentException();
    }
    if (contains(i)) {
      throw new IllegalArgumentException("index is already in the priority queue");
    }
    n++;
    qp[i] = n;
    pq[n] = i;
    keys[i] = k;
    swim(n);
  }

  public int minIndex() {
    if (n == 0) {
      throw new NoSuchElementException("Priority queue underflow");
    }
    return pq[1];
  }

  public K minKey() {
    if (n == 0) {
      throw new NoSuchElementException("Priority queue underflow");
    }
    return keys[pq[1]];
  }

  public int delMin() {
    if (n == 0) {
      throw new NoSuchElementException("Priority queue underflow");
    }
    int min = pq[1];
    exch(1, n--);
    sink(1);
    assert min == pq[n + 1];
    qp[min] = -1; // delete
    keys[min] = null; // to help with garbage collection
    pq[n + 1] = -1; // not needed
    return min;
  }

  public K keyOf(int i) {
    if (i < 0 || i >= maxN) {
      throw new IllegalArgumentException();
    }
    if (!contains(i)) {
      throw new NoSuchElementException("index is not in the priority queue");
    } else {
      return keys[i];
    }
  }

//  @Deprecated
//  public void changeKey(int i, K k) {
//    changeKey(i, k);
//  }

  public void changeKey(int i, K k) {
    if (i < 0 || i >= maxN) {
      throw new IllegalArgumentException();
    }
    if (!contains(i)) {
      throw new NoSuchElementException("index is not in the priority queue");
    }
    keys[i] = k;
    swim(qp[i]);
    sink(qp[i]);
  }

  public void decreaseKey(int i, @NotNull K k) {
    if (i < 0 || i >= maxN) {
      throw new IllegalArgumentException();
    }
    if (!contains(i)) {
      throw new NoSuchElementException("index is not in the priority queue");
    }
    if (keys[i].compareTo(k) <= 0) {
      throw new IllegalArgumentException(
          "Calling decreaseKey() with given argument would not strictly decrease the k");
    }
    keys[i] = k;
    swim(qp[i]);
  }

  public void increaseKey(int i, @NotNull K k) {
    if (i < 0 || i >= maxN) {
      throw new IllegalArgumentException();
    }
    if (!contains(i)) {
      throw new NoSuchElementException("index is not in the priority queue");
    }
    if (keys[i].compareTo(k) >= 0) {
      throw new IllegalArgumentException(
          "Calling increaseKey() with given argument would not strictly increase the k");
    }
    keys[i] = k;
    sink(qp[i]);
  }

  public void delete(int i) {
    if (i < 0 || i >= maxN) {
      throw new IllegalArgumentException();
    }
    if (!contains(i)) {
      throw new NoSuchElementException("index is not in the priority queue");
    }
    int index = qp[i];
    exch(index, n--);
    swim(index);
    sink(index);
    keys[i] = null;
    qp[i] = -1;
  }

  private boolean greater(int i, int j) {
    return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
  }

  private void exch(int i, int j) {
    int swap = pq[i];
    pq[i] = pq[j];
    pq[j] = swap;
    qp[pq[i]] = i;
    qp[pq[j]] = j;
  }

  private void swim(int k) {
    while (k > 1 && greater(k / 2, k)) {
      exch(k, k / 2);
      k = k / 2;
    }
  }

  private void sink(int k) {
    while (2 * k <= n) {
      int j = 2 * k;
      if (j < n && greater(j, j + 1)) {
        j++;
      }
      if (!greater(k, j)) {
        break;
      }
      exch(k, j);
      k = j;
    }
  }

  @NotNull
  public Iterator<Integer> iterator() {
    return new HeapIterator();
  }

  private class HeapIterator implements Iterator<Integer> {

    // create a new pq
    private IndexMinPQ<K> copy;

    // add all elements to copy of heap
    // takes linear time since already in heap order so no keys move
    public HeapIterator() {
      copy = new IndexMinPQ<K>(pq.length - 1);
      for (int i = 1; i <= n; i++) {
        copy.insert(pq[i], keys[pq[i]]);
      }
    }

    public boolean hasNext() {
      return !copy.isEmpty();
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public Integer next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return copy.delMin();
    }
  }
}
