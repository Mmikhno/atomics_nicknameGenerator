import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

public class NickNameGenerator {
    private static Random random = new Random();
    private static AtomicInteger COUNT_3 = new AtomicInteger(0);
    private static AtomicInteger COUNT_4 = new AtomicInteger(0);
    private static AtomicInteger COUNT_5 = new AtomicInteger(0);
    private static int QUANTITY = 100_000;

    private static String[] texts = new String[QUANTITY];
    private static List<Thread> threads = new ArrayList<>();

    public static void main(String... args) throws InterruptedException {
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        Thread t1 = ThreadGenerator.generateThread(TypeOfCheck.PALINDROME);
        Thread t2 = ThreadGenerator.generateThread(TypeOfCheck.SAME_LETTER);
        Thread t3 = ThreadGenerator.generateThread(TypeOfCheck.ASC_ORDER);
        startThread(threads);
        print(3, COUNT_3.get());
        print(4, COUNT_4.get());
        print(5, COUNT_5.get());
    }

    public static String generateText(String letters, int length) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    protected static void print(int len, int result) {
        String template = "Красивых слов с длиной %d: %d\n";
        System.out.printf(template, len, result);
    }

    protected static void startThread(List<Thread> threads) throws InterruptedException {
        for (Thread t : threads) {
            t.start();
        }
        for (int i = threads.size() - 1; i >= 0; i--) {
            threads.get(i).join();
        }
    }

    protected static void incrementCounter(String text) {
        switch (text.length()) {
            case 3 -> COUNT_3.incrementAndGet();
            case 4 -> COUNT_4.incrementAndGet();
            case 5 -> COUNT_5.incrementAndGet();
        }
    }

    protected static boolean isPalindrome(String string) {
        int len = string.length();
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != string.charAt(len - i - 1)) {
                return false;
            }
        }
        return true;
    }

    protected static boolean isSameLetter(String word) {
        return word.isEmpty() ? true : word.chars()
                .allMatch(i -> i == word.charAt(0));
    }

    protected static boolean isAscOrder(String word) {
        char[] wordChars = word.toCharArray();
        Arrays.sort(wordChars);
        return new String(wordChars).equals(word);
    }

    static final class ThreadGenerator {
        private static TypeOfCheck type;

        static Thread generateThread(TypeOfCheck type) {
            Thread thread = null;
            switch (type) {
                case PALINDROME -> {
                    thread = new PalindromeThread();
                }
                case SAME_LETTER -> {
                    thread = new SameLetterThread();
                }
                case ASC_ORDER -> {
                    thread = new AscOrderThread();
                }
            }
            threads.add(thread);
            return thread;
        }
    }

    static final class PalindromeThread extends Thread {
        @Override
        public void run() {
            for (String s : texts) {
                if (isPalindrome(s)) {
                    incrementCounter(s);
                }
            }
        }
    }

    static final class SameLetterThread extends Thread {

        @Override
        public void run() {
            for (String s : texts) {
                if (isSameLetter(s)) {
                    incrementCounter(s);
                }
            }
        }
    }

    static final class AscOrderThread extends Thread {
        @Override
        public void run() {
            for (String s : texts) {
                if (isAscOrder(s)) {
                    incrementCounter(s);
                }
            }
        }
    }
}
