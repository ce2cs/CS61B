public class ArrayDequeTest {

    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    public static boolean checkEqual(String expected, String actual) {
        if (expected.equals(actual)) {
            return true;
        } else {
            System.out.println("get() returned " + actual + ", but expected: " + expected);
            return false;
        }
    }

    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    /**
     * Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     * <p>
     * && is the "and" operation.
     */
    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        boolean passed = checkEmpty(true, ad1.isEmpty());

        for (int i = 0; i < 10; i++) {
            ad1.addLast(i);
        }
        for (int i = 10; i < 20; i++) {
            ad1.addFirst(i);
        }
        for (int i = 20; i < 30; i++) {
            ad1.addFirst(i);
        }
        for (int i = 30; i < 50; i++) {
            ad1.addLast(i);
        }
        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        passed = checkSize(50, ad1.size()) && passed;
        System.out.println("Printing out deque: ");
        ad1.printDeque();

        printTestStatus(passed);
    }

    /**
     * Adds an item, then removes an item, and ensures that dll is empty afterwards.
     */
    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            ad1.addLast(i);
        }
        for (int i = 10; i < 20; i++) {
            ad1.addFirst(i);
        }
        for (int i = 20; i < 30; i++) {
            ad1.addFirst(i);
        }
        for (int i = 30; i < 50; i++) {
            ad1.addLast(i);
        }
        for (int i = 0; i < 30; i++) {
            ad1.removeFirst();
        }
        for (int i = 30; i < 50; i++) {
            ad1.removeLast();
        }
    }

    public static void getTest() {
        System.out.println("Running add/remove test.");
        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque<String> lld1 = new ArrayDeque<String>();
        lld1.addFirst("front");
        lld1.addLast("middle");
        lld1.addLast("back");
        boolean passed = checkEqual("front", lld1.get(0));
        passed = checkEqual("middle", lld1.get(1)) && passed;
        passed = checkEqual("back", lld1.get(2)) && passed;
        printTestStatus(passed);
    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        getTest();
    }
}