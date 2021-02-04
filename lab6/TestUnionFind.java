import org.junit.Test;

import static org.junit.Assert.*;

public class TestUnionFind {
    UnionFind uf = new UnionFind(5); // 0, 1, 2, 3 ,4
    UnionFind uf2 = new UnionFind(15); // 0, 1, 2, 3 ,4

    @Test
    public void testConnect() {
        // 0, 1, 2, 3, 4
        int[] expected = {-1, -1, -1, -1, -1};
        assertFalse(uf.isConnected(3, 1));
        assertEquals(uf.parent(3), -1);
        assertArrayEquals(expected, uf.parent);
        uf.connect(1, 3);
        expected[3] = -2;
        expected[1] = 3;
        assertArrayEquals(expected, uf.parent); // -1, 3, -1, -2, -1
        assertTrue(uf.isConnected(3, 1));
        uf.connect(2, 4);
        expected[4] = -2;
        expected[2] = 4;
        assertArrayEquals(expected, uf.parent); // -1, 3, 4, -2, -2
        assertTrue(uf.isConnected(2, 4));
        uf.connect(3, 4); // -1, 3, 4, 4, -4
        expected[3] = 4;
        expected[4] = -4;
        assertTrue(uf.isConnected(3, 4));
        assertTrue(uf.isConnected(1, 4));
        assertArrayEquals(expected, uf.parent);
        uf.connect(0, 1); // 4, 3, 4, 4, -5
        expected[0] = 4;
        expected[4] = -5;
        assertTrue(uf.isConnected(0, 3));
        assertArrayEquals(expected, uf.parent);
    }

    @Test
    public void testIsConnected() {

    }

    @Test
    public void testParent() {
        uf.parent[1] = -2;
        uf.parent[3] = 1;
        assertEquals(uf.parent(3), 1);
    }

    @Test
    public void testFind() {
        assertEquals(uf.find(3), 3);
        uf.parent[1] = -2;
        uf.parent[3] = 1;
        assertEquals(uf.find(3), 1);
    }

    @Test
    public void testPathCompression() {
        uf2.connect(0, 1);
        uf2.connect(3, 5);
        uf2.connect(4, 6);
        uf2.connect(3, 3);
        uf2.connect(2, 1);
        uf2.connect(6, 3);
        uf2.connect(0, 2);
        uf2.connect(7, 8);
        uf2.connect(4, 8);
    }
}
