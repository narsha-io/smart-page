package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.exceptions.InternalException;
import java.util.Set;

/** Filter descriptor that must be operated on the datasource */
public interface Filter {

  /**
   * identifier used to check if we must apply this filter (example: equals,in,gt ...)
   *
   * @return alias
   */
  String getFilterAlias();

  /**
   * Classes which are supported by the current filter
   *
   * @return class descriptors
   */
  Set<Class<?>> getSupportedInputClasses();

  /**
   * Check is the current filter is able to parse the values in order to be used into the filter
   *
   * @param objectMapper use for data conversion
   * @param targetClass target java attribut class
   * @param values String[] all possibles values
   * @return the parsed value
   * @param <T> type of targeted value
   */
  default <T> T getParsedValue(ObjectMapper objectMapper, Class<T> targetClass, String[] values) {
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

  /**
   * Parse the values in order to be used into the filter
   *
   * @param objectMapper use for data conversion
   * @param targetClass target java attribut class
   * @param values String[] all possibles values
   * @return the parsed value
   * @param <T> type of targeted value
   */
  default <T> T parse(ObjectMapper objectMapper, Class<T> targetClass, String[] values) {
    return objectMapper.convertValue(values[0], targetClass);
  }

  /**
   * can this filter manage more than one value
   *
   * @return filter capacity
   */
  default boolean isMultiValueSupported() {
    return false;
  }
}
