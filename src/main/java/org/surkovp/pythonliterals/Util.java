package org.surkovp.pythonliterals;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static Map<String, List<Integer>> getLiteralLines(@NotNull Stream<@NotNull String> lines) {
        class Literal {
            private final String literal;
            private final int line;

            public Literal(@NotNull String literal, int line) {
                this.line = line;
                this.literal = literal;
            }

            public int getLine() {
                return line;
            }

            @NotNull
            public String getLiteral() {
                return literal;
            }
        }

        return lines.flatMap(new Function<String, Stream<Literal>>() {
            private int lineNumber = 0;

            @Override
            public Stream<Literal> apply(String line) {
                final int lineNumberFinalCopy = lineNumber;
                Stream<Literal> literals = getLiterals(line).stream()
                        .map(literal -> new Literal(literal, lineNumberFinalCopy));
                lineNumber++;
                return literals;
            }
        }).collect(Collectors.groupingBy(
                Literal::getLiteral,
                Collectors.mapping(Literal::getLine, Collectors.toList())
        ));
    }

    public static Map<String, List<Integer>> getLiteralLinesOccurringAtLeastOnce(@NotNull Stream<@NotNull String> lines) {
        return getLiteralLines(lines).entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
