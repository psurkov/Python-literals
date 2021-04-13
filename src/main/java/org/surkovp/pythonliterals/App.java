package org.surkovp.pythonliterals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws IOException {
        if (args == null || args.length != 1) {
            throw new IllegalArgumentException("Exactly one argument must be passed");
        }
        Stream<String> lines = Files.lines(Paths.get(args[0]));
        Map<String, List<Integer>> literalsMap = Util.getLiteralLinesOccurringAtLeastOnce(lines);
        for (Map.Entry<String, List<Integer>> entry : literalsMap.entrySet()) {
            String prefix = "Lines with '" + entry.getKey() + "': ";
            System.out.println(
                    entry.getValue().stream()
                            .map(String::valueOf)
                            .distinct()
                            .collect(Collectors.joining(", ", prefix, ""))
            );
        }
    }
}
