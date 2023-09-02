package io.narsha.smarpage.core.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.filters.EqualsFilterFactory;
import io.narsha.smartpage.core.filters.FilterFactory;
import org.junit.jupiter.api.Test;

class EqualsFilterTest {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final FilterFactory equalsFilterFactory = new EqualsFilterFactory();

  @Test
  void emptyString() {
    final var filter = equalsFilterFactory.get(String.class);
    filter.parse(objectMapper, new String[] {""});
    assertThat(filter.getValue()).isEqualTo("");
  }

  @Test
  void simpleString() {
    final var filter = equalsFilterFactory.get(String.class);
    filter.parse(objectMapper, new String[] {"test"});
    assertThat(filter.getValue()).isEqualTo("test");
  }

  @Test
  void multipleString() {
    final var filter = equalsFilterFactory.get(String.class);
    filter.parse(objectMapper, new String[] {"test1", "test2"});
    assertThat(filter.getValue()).isEqualTo("test1,test2");
  }

  @Test
  void emptyLong() {
    final var filter = equalsFilterFactory.get(Long.class);
    filter.parse(objectMapper, new String[] {""});
    assertThat(filter.getValue()).isNull();
  }

  @Test
  void simpleLong() {
    final var filter = equalsFilterFactory.get(Long.class);
    filter.parse(objectMapper, new String[] {"1"});
    assertThat(filter.getValue()).isEqualTo(1L);
  }

  @Test
  void simpleInvalidLong() {
    final var filter = equalsFilterFactory.get(Long.class);
    assertThrows(
        IllegalArgumentException.class, () -> filter.parse(objectMapper, new String[] {"tt"}));
  }

  @Test
  void multipleLong() {
    final var filter = equalsFilterFactory.get(Long.class);
    assertThrows(
        IllegalArgumentException.class, () -> filter.parse(objectMapper, new String[] {"1", "2"}));
  }

  @Test
  void multipleInvalidLong() {
    final var filter = equalsFilterFactory.get(Long.class);
    assertThrows(
        IllegalArgumentException.class,
        () -> filter.parse(objectMapper, new String[] {"1", "toto"}));
  }
}
