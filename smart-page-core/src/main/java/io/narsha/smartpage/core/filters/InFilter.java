package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InFilter<T> extends FilterParser<T, Set<T>> {

  private Set<T> value;

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

  public Set<T> getValue() {
    return this.value;
  }
}
