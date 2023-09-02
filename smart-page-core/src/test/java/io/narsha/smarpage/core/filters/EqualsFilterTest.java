package io.narsha.smarpage.core.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.filters.EqualsFilter;
import org.junit.jupiter.api.Test;

public class EqualsFilterTest {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void emptyString() {
    final var filter = new EqualsFilter();
    filter.parse(objectMapper, String.class, new String[] {""});
    assertThat(filter.getValue()).isEqualTo("");
  }

  @Test
  public void simpleString() {
    final var filter = new EqualsFilter();
    filter.parse(objectMapper, String.class, new String[] {"test"});
    assertThat(filter.getValue()).isEqualTo("test");
  }

  @Test
  public void multipleString() {
    final var filter = new EqualsFilter();
    filter.parse(objectMapper, String.class, new String[] {"test1", "test2"});
    assertThat(filter.getValue()).isEqualTo("test1,test2");
  }

  @Test
  public void emptyLong() {
    final var filter = new EqualsFilter();
    filter.parse(objectMapper, Long.class, new String[] {""});
    assertThat(filter.getValue()).isEqualTo(null);
  }

  @Test
  public void simpleLong() {
    final var filter = new EqualsFilter();
    filter.parse(objectMapper, Long.class, new String[] {"1"});
    assertThat(filter.getValue()).isEqualTo(1L);
  }

  @Test
  public void simpleInvalidLong() {
    final var filter = new EqualsFilter();
    filter.parse(objectMapper, Long.class, new String[] {""});
    assertThat(filter.getValue()).isEqualTo(null);
  }

  @Test
  public void multipleLong() {
    final var filter = new EqualsFilter();
    assertThrows(
        IllegalArgumentException.class,
        () -> filter.parse(objectMapper, Long.class, new String[] {"1", "2"}));
  }

  @Test
  public void multipleInvalidLong() {
    final var filter = new EqualsFilter();
    assertThrows(
        IllegalArgumentException.class,
        () -> filter.parse(objectMapper, Long.class, new String[] {"1", "toto"}));
  }
}
