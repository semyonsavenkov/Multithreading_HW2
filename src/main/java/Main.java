import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {

            Runnable logic = () -> {
                String route = generateRoute("RLRFR", 100);
                int maxSize = 0;
                int currentSize = 0;
                int quantityOfR = 0;
                for (int x = 0; x < route.length(); x++) {
                    if (route.charAt(x) == 'R') {
                        currentSize++;
                        quantityOfR++;
                        maxSize = Math.max(currentSize, maxSize);
                    } else {
                        currentSize = 0;
                    }

                }
                synchronized (sizeToFreq) {
                    sizeToFreq.put(quantityOfR, maxSize);
                    sizeToFreq.notify();
                }
            };
            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        HashMap sortedMap = new HashMap<>();
        sizeToFreq.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .forEach(e -> sortedMap.put(e.getKey(), e.getValue()));

        System.out.println(printUserMessage(sortedMap));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static String printUserMessage(HashMap<Integer, Integer> sortedMap) {

        String result = "";
        String addition = "";

        for (Map.Entry myEntry : sortedMap.entrySet()) {
            if (result == "") {
                addition = "Самое частое количество повторений " + myEntry.getKey() +
                        " (встретилось " + myEntry.getValue() + " раз)" + "\n" + "Другие размеры:" + "\n";
            } else {
                addition = "- " + myEntry.getKey() + " (" + myEntry.getValue() + ")" + "\n";
            }
            result = result + addition;
        }
        return result;
    }

}
