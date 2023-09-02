package io.narsha.smarpage.core.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.filters.Filter;
import io.narsha.smartpage.core.filters.FilterParser;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class EqualsFilterTest {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final Supplier<FilterParser> equalsParserSupplier = Filter.EQUALS.getParser();

  @Test
  void emptyString() {
    final var filter = equalsParserSupplier.get();
    filter.parse(objectMapper, String.class, new String[] {""});
    assertThat(filter.getValue()).isEqualTo("");
  }

  @Test
  void simpleString() {
    final var filter = equalsParserSupplier.get();
    filter.parse(objectMapper, String.class, new String[] {"test"});
    assertThat(filter.getValue()).isEqualTo("test");
  }

  @Test
  void multipleString() {
    final var filter = equalsParserSupplier.get();
    filter.parse(objectMapper, String.class, new String[] {"test1", "test2"});
    assertThat(filter.getValue()).isEqualTo("test1,test2");
  }

  @Test
  void emptyLong() {
    final var filter = equalsParserSupplier.get();
    filter.parse(objectMapper, Long.class, new String[] {""});
    assertThat(filter.getValue()).isNull();
  }

  @Test
  void simpleLong() {
    final var filter = equalsParserSupplier.get();
    filter.parse(objectMapper, Long.class, new String[] {"1"});
    assertThat(filter.getValue()).isEqualTo(1L);
  }

  @Test
  void simpleInvalidLong() {
    final var filter = equalsParserSupplier.get();
    filter.parse(objectMapper, Long.class, new String[] {""});
    assertThat(filter.getValue()).isNull();
  }

  @Test
  void multipleLong() {
    final var filter = equalsParserSupplier.get();
    assertThrows(
        IllegalArgumentException.class,
        () -> filter.parse(objectMapper, Long.class, new String[] {"1", "2"}));
  }

  @Test
  void multipleInvalidLong() {
    final var filter = equalsParserSupplier.get();
    assertThrows(
        IllegalArgumentException.class,
        () -> filter.parse(objectMapper, Long.class, new String[] {"1", "toto"}));
  }
}
