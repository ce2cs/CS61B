package bearmaps;

import org.junit.Assert;
import org.junit.Test;
import edu.princeton.cs.algs4.Stopwatch;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.Random;

public class ArrayHeapMinPQTest {
    ArrayHeapMinPQ<Integer> heapq = new ArrayHeapMinPQ<>();
    NaiveMinPQ<Integer> naiveMinPQ = new NaiveMinPQ<>();

    @Test
    public void testAdd() {
        heapq.add(1, 1);
        heapq.add(2, 3);
        heapq.add(3, 10);
        heapq.add(4, 1);
        heapq.add(5, 1);
        heapq.add(6, 0);
    }

    @Test
    public void testRemove() {
        Random randomGenerator = new Random(0);
        int caseNumber = 10000;
        for (int i = 0; i < caseNumber; i++) {
            double priority = 10000 * randomGenerator.nextDouble();
            heapq.add(i, priority);
            naiveMinPQ.add(i, priority);
        }
        for (int i = 0; i < caseNumber; i++) {
            assertEquals(heapq.removeSmallest(), naiveMinPQ.removeSmallest());
        }
    }

    @Test
    public void testChangePriority() {
        Random randomGenerator = new Random(4);
        int caseNumber = 10000;
        for (int i = 0; i < caseNumber; i++) {
            double priority = 10000 * randomGenerator.nextDouble();
            heapq.add(i, priority);
            naiveMinPQ.add(i, priority);
            if (i % 2 == 0) {
                priority = 10000 * randomGenerator.nextDouble();
                heapq.changePriority(i, priority);
                naiveMinPQ.changePriority(i, priority);
            }
            if (i % 3 == 0) {
                priority = 10000 * randomGenerator.nextDouble();
                heapq.changePriority(i, priority);
                naiveMinPQ.changePriority(i, priority);
            }
        }
        for (int i = 1; i < caseNumber; i++) {
            assertEquals(heapq.contains(i), naiveMinPQ.contains(i));
            assertEquals(heapq.getSmallest(), naiveMinPQ.getSmallest());
            assertEquals(heapq.size(), naiveMinPQ.size());
            assertEquals(heapq.removeSmallest(), naiveMinPQ.removeSmallest());
            assertEquals(heapq.contains(i), naiveMinPQ.contains(i));
            assertEquals(heapq.getSmallest(), naiveMinPQ.getSmallest());
            assertEquals(heapq.size(), naiveMinPQ.size());
        }
    }

    @Test
    public void compareSpeed() {
        Stopwatch timer1 = new Stopwatch();
        Random randomGenerator = new Random(4);
        int caseNumber = 100000;
        for (int i = 0; i < caseNumber; i++) {
            double priority = 10000 * randomGenerator.nextDouble();
            heapq.add(i, priority);
            if (i % 2 == 0) {
                priority = 10000 * randomGenerator.nextDouble();
                heapq.changePriority(i, priority);
            }
            if (i % 3 == 0) {
                priority = 10000 * randomGenerator.nextDouble();
                heapq.changePriority(i, priority);
            }
        }
        for (int i = 0; i < caseNumber; i++) {
            heapq.removeSmallest();
        }
        double time1 = timer1.elapsedTime();

        Stopwatch timer2 = new Stopwatch();
        for (int i = 0; i < caseNumber; i++) {
            double priority = 10000 * randomGenerator.nextDouble();
            naiveMinPQ.add(i, priority);
            if (i % 2 == 0) {
                priority = 10000 * randomGenerator.nextDouble();
                naiveMinPQ.changePriority(i, priority);
            }
            if (i % 3 == 0) {
                priority = 10000 * randomGenerator.nextDouble();
                naiveMinPQ.changePriority(i, priority);
            }
        }
        for (int i = 0; i < caseNumber; i++) {
            naiveMinPQ.removeSmallest();
        }
        double time2 = timer2.elapsedTime();
        System.out.println(String.format("%f, %f", time1, time2));
    }
}
