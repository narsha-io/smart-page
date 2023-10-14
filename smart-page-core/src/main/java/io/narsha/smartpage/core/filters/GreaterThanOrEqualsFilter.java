package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

/**
 * class which provide some method to parse String array into a targetClass in order to apply an
 * Greater than or equals operation
 *
 * @param <T> the target property class
 */
@Getter
public class GreaterThanOrEqualsFilter<T> extends FilterParser<T, Object> {

  private Object value;

  /**
   * Basic constructor
   *
   * @param targetClass the class of the targeted property
   */
  public GreaterThanOrEqualsFilter(Class<T> targetClass) {
    super(targetClass);
  }

  @Override
  public void parse(ObjectMapper objectMapper, String[] value) {
    if (value.length > 1) {
      throw new IllegalArgumentException(); // TODO custom exception cannot manage more than one
      // element if not string
    }
    this.value = objectMapper.convertValue(value[0], targetClass);
  }
}
