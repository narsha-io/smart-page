package io.narsha.smarpage.core.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.filters.Filter;
import io.narsha.smartpage.core.filters.FilterParser;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class ContainsFilterTest {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final Supplier<FilterParser> containsParserSupplier = Filter.CONTAINS.getParser();

  @Test
  void emptyString() {
    final var filter = containsParserSupplier.get();
    filter.parse(objectMapper, String.class, new String[] {""});
    assertThat(filter.getValue()).isEqualTo("%%"); // TODO throw exception
  }

  @Test
  void simpleString() {
    final var filter = containsParserSupplier.get();
    filter.parse(objectMapper, String.class, new String[] {"test"});
    assertThat(filter.getValue()).isEqualTo("%test%");
    assertThat(filter.getSQLFragment("name")).isEqualTo(" like :name");
  }

  @Test
  void multipleString() {
    final var filter = containsParserSupplier.get();
    filter.parse(objectMapper, String.class, new String[] {"test1", "test2"});
    assertThat(filter.getValue()).isEqualTo("%test1,test2%");
  }

  @Test
  void emptyLong() {
    final var filter = containsParserSupplier.get();
    filter.parse(objectMapper, Long.class, new String[] {""});
    assertThat(filter.getValue())
        .isEqualTo("%null%"); // TODO must throw exception because mismatch type
  }

  @Test
  void simpleLong() {
    final var filter = containsParserSupplier.get();
    filter.parse(objectMapper, String.class, new String[] {"1"});
    assertThat(filter.getValue()).isEqualTo("%1%");
  }

  @Test
  void simpleInvalidLong() {
    final var filter = containsParserSupplier.get();

    assertThrows(
        IllegalArgumentException.class,
        () -> filter.parse(objectMapper, Long.class, new String[] {"toto"}));
  }

  @Test
  void multipleLong() {
    final var filter = containsParserSupplier.get();
    assertThrows(
        IllegalArgumentException.class,
        () -> filter.parse(objectMapper, Long.class, new String[] {"1", "2"}));
  }

  @Test
  void multipleInvalidLong() {
    final var filter = containsParserSupplier.get();
    assertThrows(
        IllegalArgumentException.class,
        () -> filter.parse(objectMapper, Long.class, new String[] {"1", "toto"}));
  }
}
