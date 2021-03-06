package org.surkovp.pythonliterals;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {

    public static @NotNull List<String> getLiterals(@NotNull String line) {
        char[] chars = line.toCharArray();
        boolean isEscaping = false;
        boolean isSmallQuote = false;
        boolean isBigQuote = false;
        List<String> literals = new ArrayList<>();
        StringBuilder currentQuote = new StringBuilder();
        for (char c : chars) {
            if (isEscaping) {
                isEscaping = false;
                if (isSmallQuote || isBigQuote) {
                    currentQuote.append(c);
                }
                continue;
            }
            if (c == '\'') {
                if (isSmallQuote) {
                    literals.add(currentQuote.toString());
                    currentQuote.setLength(0);
                }
                isSmallQuote = !isSmallQuote;
                continue;
            }
            if (c == '"') {
                if (isBigQuote) {
                    literals.add(currentQuote.toString());
                    currentQuote.setLength(0);
                }
                isBigQuote = !isBigQuote;
                continue;
            }
            if (c == '\\') {
                isEscaping = true;
                continue;
            }
            if (isSmallQuote || isBigQuote) {
                currentQuote.append(c);
                continue;
            }
            if (c == '#') {
                break;
            }
        }
        return literals;
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
