package io.narsha.smartpage.core.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.exceptions.InternalException;
import org.junit.jupiter.api.Test;

class ContainsFilterTest {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final Filter containsFilter = new ContainsFilter();

  @Test
  void emptyString() {
    final var value = containsFilter.getParsedValue(objectMapper, String.class, new String[] {""});
    assertThat(value).isEmpty();
  }

  @Test
  void simpleString() {
    final var value =
        containsFilter.getParsedValue(objectMapper, String.class, new String[] {"test"});
    assertThat(value).isEqualTo("test");
  }

  @Test
  void multipleString() {
    final var value =
        containsFilter.getParsedValue(objectMapper, String.class, new String[] {"test1", "test2"});
    assertThat(value).isEqualTo("test1,test2");
  }

  @Test
  void invalidLong() {
    assertThrows(
        InternalException.class,
        () -> containsFilter.getParsedValue(objectMapper, Long.class, new String[] {"ok"}));
  }
}
