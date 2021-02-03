package es.datastructur.synthesizer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    BoundedQueue<Double> bq = new ArrayRingBuffer<Double>(4);
    private double doubleMargin = 1e-12;
    @Test(expected = RuntimeException.class)
    public void testBasicOperations() {
        assertTrue(bq.isEmpty());
        bq.enqueue(9.3);    // 9.3
        bq.enqueue(15.1);   // 9.3  15.1
        bq.enqueue(31.2);   // 9.3  15.1  31.2
        assertFalse(bq.isFull());// 9.3  15.1  31.2       (returns false)
        bq.enqueue(-3.1);   // 9.3  15.1  31.2  -3.1
        assertTrue(bq.isFull());        // 9.3  15.1  31.2  -3.1 (returns true)
        bq.enqueue(-3.1);   // 9.3  15.1  31.2  -3.1
        bq.dequeue();       // 15.1 31.2  -3.1       (returns 9.3)
        double assume = bq.peek();
        assertEquals(15.1, assume, doubleMargin);
        bq.dequeue();
        bq.dequeue();
        bq.dequeue();
        bq.dequeue();
        assertTrue(bq.isEmpty());
    }

    @Test
    public void testEquals() {
        BoundedQueue<Double> anotherBq = new ArrayRingBuffer<Double>(6);
        bq.enqueue(9.3);    // 9.3
        bq.enqueue(15.1);   // 9.3  15.1
        bq.enqueue(31.2);
        anotherBq.enqueue(1.);
        anotherBq.enqueue(1.);
        anotherBq.dequeue();
        anotherBq.dequeue();
        anotherBq.enqueue(9.3);    // 9.3
        anotherBq.enqueue(15.1);   // 9.3  15.1
        anotherBq.enqueue(31.2);
        assertEquals(bq, anotherBq);
    }

    @Test
    public void testIterate() {
        bq.enqueue(1.);    // 9.3
        bq.enqueue(2.);   // 9.3  15.1
        bq.enqueue(3.);
        int i = 1;
        for (double item: bq) {
            assertEquals(item, (double) i, doubleMargin);
            i += 1;
        }
    }
}

