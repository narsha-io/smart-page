package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class EqualsFilter<T> extends FilterParser<T, Object> {

  private Object value;

  public EqualsFilter(Class<T> targetClass) {
    super(targetClass);
  }

  @Override
  public void parse(ObjectMapper objectMapper, String[] value) {
    if (value.length > 1 && !targetClass.equals(String.class)) {
      throw new IllegalArgumentException(); // TODO custom exception cannot manage more than one
      // element if not string
    } else if (value.length > 1) {
      this.value = objectMapper.convertValue(String.join(",", value), targetClass);
    } else {
      this.value = objectMapper.convertValue(value[0], targetClass);
    }
  }
}
