

import java.io.File;
import java.util.*;

public class Occurences {

    public static void main(String[] args) throws Exception {
        String filnavn = "src/LoremIpsum.txt";
        try (Scanner scanner = new Scanner(new File(filnavn))) {
            Map<String, Map<Integer, Integer>> map = new HashMap<>();
            int linjeNr = 0;

            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine(), ++linjeNr, map);
            }

            List<Map.Entry<String, Integer>> totaleForekomster = kalkulerTotaleForekomster(map);
            totaleForekomster.sort((a, b) -> {
                int antallSammenligning = b.getValue().compareTo(a.getValue());
                if (antallSammenligning == 0) {
                    return a.getKey().compareTo(b.getKey());
                }
                return antallSammenligning;
            });

            printResultat(map, totaleForekomster);
            System.out.println();
            printOppsummering(map, totaleForekomster);
        }
    }
    private static void processLine(String linje, int linjeNr, Map<String, Map<Integer, Integer>> map) {
        String[] ordTab = linje.split("\\W+");
        for (String ord : ordTab) {
            ord = ord.toLowerCase();
            if (!ord.isEmpty()) {
                map.computeIfAbsent(ord, k -> new TreeMap<>())
                        .merge(linjeNr, 1, Integer::sum);
            }
        }
    }
    private static List<Map.Entry<String, Integer>> kalkulerTotaleForekomster(Map<String, Map<Integer, Integer>> map) {
        List<Map.Entry<String, Integer>> totaleForekomster = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, Integer>> entry : map.entrySet()) {
            int total = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
            totaleForekomster.add(new AbstractMap.SimpleEntry<>(entry.getKey(), total));
        }
        return totaleForekomster;
    }
    private static void printResultat(Map<String, Map<Integer, Integer>> map, List<Map.Entry<String, Integer>> totaleForekomster) {
        for (Map.Entry<String, Integer> entry : totaleForekomster) {
            String ord = entry.getKey();
            int total = entry.getValue();
            System.out.println("Ord: " + ord);

            map.get(ord).forEach((linjeNr, antall) ->
                    System.out.println(" Linje " + linjeNr + ": " + antall + " forekomster")
            );

            System.out.println("Ordet: '" + ord + "' kom totalt " + total + " ganger.");
        }
    }
    private static void printOppsummering(Map<String, Map<Integer, Integer>> map, List<Map.Entry<String, Integer>> totalOccurences) {
        for (Map.Entry<String, Integer> entry : totalOccurences) {
            String ord = entry.getKey();
            int total = entry.getValue();
            System.out.println("Ordet: '" + ord + "' kom " + total + " ganger.");
        }
    }
}

