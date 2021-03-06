package fitnesse.wikitext.parser;

import org.junit.Test;

import static fitnesse.wikitext.parser.ParserTestHelper.assertParsesWithOffset;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test focuses on getting the right text offsets aligned with the symbols.
 * Having offsets is of importance if we want to trace back a symbol to a piece of
 * source (wiki) text. E.g. when parsing text in an IDE.
 * The most important ones are: wiki words, essential markup (italic, bold) and
 * tables.
 */
public class SymbolOffsetTest {

  @Test
  public void wikiMarkup() {
    assertParsesWithOffset(
    //      0         1         2
    //      0123456789012345678901
            "This ''is'' a WikiWord",
            "SymbolList<0..22>[" +
                    "Text<0..4>, Whitespace<4..5>, Italic<5..11>[SymbolList<7..11>[Text<7..9>]]," +
                    " Whitespace<11..12>, Text<12..13>, Whitespace<13..14>, WikiWord<14..22>]");
  }

  @Test
  public void oneLineTable() {
    assertParsesWithOffset(
    //       012345678
            "| table |",
            "SymbolList<0..9>[" +
                    "Table<0..9>[TableRow<1..9>[" +
                        "TableCell<1..9>[Whitespace<1..2>, Text<2..7>, Whitespace<7..8>]]]]");
  }

  @Test
  public void multiLineTable() {
    assertParsesWithOffset(
    //       0         1          2         3         4
    //       012345678901234567 89012345678901234567890123
            "| script:fooBar |\n| ensure | do something |\n",
            "SymbolList<0..44>[" +
                    "Table<0..44>[" +
                    "TableRow<1..19>[TableCell<1..19>[Whitespace<1..2>, Text<2..8>, Colon<8..9>, Text<9..15>, Whitespace<15..16>]], " +
                    "TableRow<19..44>[TableCell<19..28>[Whitespace<19..20>, Text<20..26>, Whitespace<26..27>], " +
                        "TableCell<28..44>[Whitespace<28..29>, Text<29..31>, Whitespace<31..32>, Text<32..41>, Whitespace<41..42>]]]]");
  }

  @Test
  public void escapedLineTable() {
    assertParsesWithOffset(
            "-!| table |",
            "SymbolList<0..11>[" +
                    "Table<0..11>[TableRow<3..11>[" +
                    "TableCell<3..11>[Text<3..10>]]]]");
  }

  @Test
  public void literalText() {
    assertParsesWithOffset(
            "Some !-literal text-!",
            "SymbolList<0..21>[Text<0..4>, Whitespace<4..5>, Literal<7..19>]");
  }

  @Test
  public void comment() {
    assertParsesWithOffset(
            "# a comment",
            "SymbolList<0..11>[Comment<0..11>[Text<-1..-1>]]");
  }

  @Test
  public void onlyHasOffsetIfStartAndEndOffsetIsSet() {
    Symbol noOffsets = new Symbol(SymbolType.Text, "text");
    Symbol onlyStartOffset = new Symbol(SymbolType.Text, "text", 0);
    Symbol startAndEndOffset = new Symbol(SymbolType.Text, "text", 0);
    startAndEndOffset.setEndOffset(4);

    assertFalse(noOffsets.hasOffset());
    assertTrue(onlyStartOffset.hasOffset());
    assertTrue(startAndEndOffset.hasOffset());
  }
}
