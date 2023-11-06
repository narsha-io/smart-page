package io.narsha.smartpage.core.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.exceptions.InternalException;
import org.junit.jupiter.api.Test;

class EqualsFilterTest {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final Filter equalsFilter = new EqualsFilter();

  @Test
  void emptyString() {
    final var value = equalsFilter.getParsedValue(objectMapper, String.class, new String[] {""});
    assertThat(value).isEmpty();
  }

  @Test
  void simpleString() {
    final var value =
        equalsFilter.getParsedValue(objectMapper, String.class, new String[] {"test"});
    assertThat(value).isEqualTo("test");
  }

  @Test
  void multipleString() {
    assertThrows(
        InternalException.class,
        () ->
            equalsFilter.getParsedValue(
                objectMapper, String.class, new String[] {"test1", "test2"}));
  }

  @Test
  void emptyLong() {
    final var value = equalsFilter.getParsedValue(objectMapper, Long.class, new String[] {""});
    assertThat(value).isNull();
  }

  @Test
  void simpleLong() {
    final var value = equalsFilter.getParsedValue(objectMapper, Long.class, new String[] {"1"});
    assertThat(value).isEqualTo(1L);
  }

  @Test
  void simpleInvalidLong() {
    assertThrows(
        InternalException.class,
        () -> equalsFilter.getParsedValue(objectMapper, Long.class, new String[] {"tt"}));
  }

  @Test
  void multipleLong() {
    assertThrows(
        InternalException.class,
        () -> equalsFilter.getParsedValue(objectMapper, Long.class, new String[] {"1", "2"}));
  }

  @Test
  void multipleInvalidLong() {
    assertThrows(
        InternalException.class,
        () -> equalsFilter.getParsedValue(objectMapper, Long.class, new String[] {"1", "toto"}));
  }
}
