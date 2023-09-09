package io.narsha.smartpage.core.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class ContainsFilterTest {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final FilterFactory containsFilterFactory = new ContainsFilterFactory();

  @Test
  void emptyString() {
    final var filter = containsFilterFactory.get(String.class);
    filter.parse(objectMapper, new String[] {""});
    final String value = (String) filter.getValue();
    assertThat(value).isEmpty();
  }

  @Test
  void simpleString() {
    final var filter = containsFilterFactory.get(String.class);
    filter.parse(objectMapper, new String[] {"test"});
    assertThat(filter.getValue()).isEqualTo("test");
  }

  @Test
  void multipleString() {
    final var filter = containsFilterFactory.get(String.class);
    filter.parse(objectMapper, new String[] {"test1", "test2"});
    assertThat(filter.getValue()).isEqualTo("test1,test2");
  }

  @Test
  void invalidLong() {
    assertThrows(IllegalArgumentException.class, () -> containsFilterFactory.get(Long.class));
  }
}
