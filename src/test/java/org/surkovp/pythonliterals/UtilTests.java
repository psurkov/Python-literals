package org.surkovp.pythonliterals;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilTests {

    @Test
    void getLiteralsEmptyStringTest() {
        assertEquals(
                Collections.emptyList(),
                Util.getLiterals(""));
    }

    @Test
    void getLiteralsEmptyLiteralTest() {
        assertEquals(
                Collections.singletonList(""),
                Util.getLiterals("''")
        );
        assertEquals(
                Collections.singletonList(""),
                Util.getLiterals("\"\"")
        );
    }

    @Test
    void getLiteralsWholeStringLiteralTest() {
        assertEquals(
                Collections.singletonList("literal"),
                Util.getLiterals("'literal'"));
        assertEquals(
                Collections.singletonList("literal"),
                Util.getLiterals("\"literal\""));
    }

    @Test
    void getLiteralsTextWithLiteralsAndNonLiteralsTest() {
        assertEquals(
                List.of("literal", "literal2"),
                Util.getLiterals("text\"literal\"text'literal2'")
        );
    }

    @Test
    void getLiteralsDuplicatesLiteralsTest() {
        assertEquals(
                List.of("literal1", "literal2", "literal1"),
                Util.getLiterals("text text'literal1'text\"literal2\"texttext\"literal1\"")
        );
    }


    @Test
    void getLiteralsDifferentQuotesMarksTest() {
        assertEquals(
                Collections.emptyList(),
                Util.getLiterals("'literal\"")
        );
        assertEquals(
                Collections.emptyList(),
                Util.getLiterals("\"literal'")
        );
    }

    @Test
    void getLiteralsWholeLineCommentTest() {
        assertEquals(
                Collections.emptyList(),
                Util.getLiterals("# 'literal' ")
        );
    }

    @Test
    void getLiteralsLiteralAndCommentThenTest() {
        assertEquals(
                Collections.singletonList("literal1"),
                Util.getLiterals("'literal1'# 'literal2' ")
        );
    }

    @Test
    void getLiteralsMultipleCommentLineTest() {
        assertEquals(
                Collections.singletonList("literal1"),
                Util.getLiterals("'literal1'# 'literal2' 'literal3' \"literal4\"")
        );
    }

    @Test
    void getLiteralLinesEmptyTextTest() {
        assertEquals(
                Collections.emptyMap(),
                Util.getLiteralLines(Stream.empty())
        );
    }

    @Test
    void getLiteralLinesTest() {
        assertEquals(
                Map.of(
                        "literal", List.of(0, 1, 4),
                        "literal2", Collections.singletonList(1),
                        "literal4", List.of(2, 2)),
                Util.getLiteralLines(Stream.of(
                        "text 'literal' text #text 'literal2' # 'literal2'",
                        "text \"literal2\" \"literal\"",
                        "'literal4' text 'literal4'",
                        "text",
                        "'literal'")
                )
        );
    }

    @Test
    void getLiteralLinesOccurringAtLeastOnceTest() {
        assertEquals(
                Map.of(
                        "literal", List.of(0, 1, 4),
                        "literal4", List.of(2, 2)),
                Util.getLiteralLinesOccurringAtLeastOnce(Stream.of(
                        "text 'literal' text #text 'literal2' # 'literal2'",
                        "text \"literal2\" \"literal\"",
                        "'literal4' text 'literal4'",
                        "text",
                        "'literal'")
                )
        );
    }
}