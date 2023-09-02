package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EqualsFilter extends FilterParser {

  private Object value;

  @Override
  public void parse(ObjectMapper objectMapper, Class<?> targetClass, String[] value) {
    this.value = objectMapper.convertValue(String.join(",", value), targetClass);
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format(" = :%s", property);
  }

  public Object getValue() {
    return this.value;
  }
}
