package io.narsha.smarpage.core.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.filters.FilterFactory;
import io.narsha.smartpage.core.filters.InFilterFactory;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InFilterTest {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final FilterFactory inFilterFactory = new InFilterFactory();

  @Test
  void emptyString() {
    final var filter = inFilterFactory.get(String.class);
    filter.parse(objectMapper, new String[] {""});
    assertThat(filter.getValue()).isEqualTo(Set.of(""));
  }

  @Test
  void simpleString() {
    final var filter = inFilterFactory.get(String.class);
    filter.parse(objectMapper, new String[] {"test"});
    assertThat(filter.getValue()).isEqualTo(Set.of("test"));
  }

  @Test
  void multipleString() {
    final var filter = inFilterFactory.get(String.class);
    filter.parse(objectMapper, new String[] {"test1", "test2"});
    assertThat(filter.getValue()).isEqualTo(Set.of("test1", "test2"));
  }

  @Test
  void emptyLong() {
    final var filter = inFilterFactory.get(Long.class);
    filter.parse(objectMapper, new String[] {""});
    final var set = new HashSet<Long>();
    set.add(null);
    assertThat(filter.getValue()).isEqualTo(set);
  }

  @Test
  void simpleLong() {
    final var filter = inFilterFactory.get(Long.class);
    filter.parse(objectMapper, new String[] {"1"});
    assertThat(filter.getValue()).isEqualTo(Set.of(1L));
  }

  @Test
  void simpleInvalidLong() {
    final var filter = inFilterFactory.get(Long.class);

    assertThrows(
        IllegalArgumentException.class, () -> filter.parse(objectMapper, new String[] {"toto"}));
  }

  @Test
  void multipleLong() {
    final var filter = inFilterFactory.get(Long.class);
    filter.parse(objectMapper, new String[] {"1", "2"});
    assertThat(filter.getValue()).isEqualTo(Set.of(1L, 2L));
  }

  @Test
  void multipleInvalidLong() {
    final var filter = inFilterFactory.get(Long.class);
    assertThrows(
        IllegalArgumentException.class,
        () -> filter.parse(objectMapper, new String[] {"1", "toto"}));
  }
}
