package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InFilter extends FilterParser {

  private Set<Object> value;

  @Override
  public void parse(ObjectMapper objectMapper, Class<?> targetClass, String[] value) {
    if (value == null) {
      this.value = Set.of();
      return;
    }

    this.value =
        Stream.of(value)
            .map(v -> objectMapper.convertValue(v, targetClass))
            .collect(Collectors.toSet());
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format(" in (:%s)", property);
  }

  public Object getValue() {
    return this.value;
  }
}
