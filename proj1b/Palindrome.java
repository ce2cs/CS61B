import java.util.Calendar;

public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<>();
        char[] wordChars = word.toCharArray();
        for (char c : wordChars) {
            deque.addLast(c);
        }
        return deque;
    }

    public boolean isPalindrome(String word) {
        if (word == null) {
            return false;
        }
        Deque deque = wordToDeque(word);
        return DequeIsPalindrome(deque);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word == null) {
            return false;
        }
        Deque deque = wordToDeque(word);
        if (cc == null) {
            return DequeIsPalindrome(deque);
        }
        return DequeIsPalindrome(deque, cc);
    }

    private boolean DequeIsPalindrome(Deque<Character> deque) {
        if (deque.size() <= 1) {
            return true;
        }
        char lastChar = deque.removeLast();
        char FirstChar = deque.removeFirst();
        if (lastChar == FirstChar) {
            return DequeIsPalindrome(deque);
        } else {
            return false;
        }
    }

    private boolean DequeIsPalindrome(Deque<Character> deque, CharacterComparator cc) {
        if (deque.size() <= 1) {
            return true;
        }
        char lastChar = deque.removeLast();
        char FirstChar = deque.removeFirst();
        if (cc.equalChars(lastChar, FirstChar)) {
            return DequeIsPalindrome(deque, cc);
        } else {
            return false;
        }
    }
}
