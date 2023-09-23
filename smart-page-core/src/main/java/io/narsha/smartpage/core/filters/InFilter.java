package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * class which provide some method to parse String array into a targetClass in order to apply an In
 * operation
 *
 * @param <T> the target property class
 */
public class InFilter<T> extends FilterParser<T, Set<T>> {

  /** The unique parsed value to find in the final query */
  private Set<T> value;

  /**
   * Basic constructor
   *
   * @param targetClass DTO class
   */
  public InFilter(Class<T> targetClass) {
    super(targetClass);
  }

  @Override
  public void parse(ObjectMapper objectMapper, String[] value) {
    if (value == null) {
      this.value = Set.of();
      return;
    }

    this.value =
        Stream.of(value)
            .map(v -> objectMapper.convertValue(v, targetClass))
            .collect(Collectors.toSet());
  }

  /**
   * Get the parsed values
   *
   * @return parsed values
   */
  public Set<T> getValue() {
    return this.value;
  }
}
