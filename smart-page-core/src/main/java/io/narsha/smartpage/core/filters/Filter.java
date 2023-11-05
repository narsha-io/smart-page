package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.exceptions.InternalException;
import java.util.Set;

public abstract class Filter {

  public abstract String getFilterAlias();

  protected abstract Set<Class<?>> getSupportedInputClasses();

  public <T> T getParsedValue(ObjectMapper objectMapper, Class<T> targetClass, String[] values) {
    final var match =
        getSupportedInputClasses().stream().anyMatch(clazz -> clazz.isAssignableFrom(targetClass));

    if (!match) {
      throw new InternalException();
    }

    if (!isMultiValueSupported() && values.length > 1) {
      throw new InternalException();
    }

    try {
      return parse(objectMapper, targetClass, values);
    } catch (IllegalArgumentException e) {
      throw new InternalException(e.getMessage(), e);
    }
  }

  protected <T> T parse(ObjectMapper objectMapper, Class<T> targetClass, String[] values) {
    return objectMapper.convertValue(values[0], targetClass);
  }

  protected boolean isMultiValueSupported() {
    return false;
  }
}
