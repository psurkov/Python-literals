package org.surkovp.pythonliterals;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {
    private static final Pattern literalPattern = Pattern.compile("(['\"]).*?\\1");

    public static @NotNull List<String> getLiterals(@NotNull String line) {
        return literalPattern.matcher(
                line.split("#", 2)[0]
        ).results()
                .map(MatchResult::group)
                .map(s -> s.substring(1, s.length() - 1))
                .collect(Collectors.toList());
    }

    public static Map<String, List<Integer>> getLiteralLines(@NotNull List<@NotNull String> lines) {
        return IntStream.range(0, lines.size())
                .boxed()
                .flatMap(
                        index -> getLiterals(lines.get(index)).stream()
                                .map(literal -> Map.entry(literal, index)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                Map.Entry::getValue, Collectors.toList())));
    }

    public static Map<String, List<Integer>> getLiteralLinesOccurringAtLeastOnce(@NotNull List<@NotNull String> lines) {
        return getLiteralLines(lines).entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
