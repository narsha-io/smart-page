package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * class which provide some method to parse String array into a targetClass in order to apply an
 * Greater than operation
 */
public class InFilter implements Filter {

  @Override
  public String getFilterAlias() {
    return "in";
  }

  @Override
  public Set<Class<?>> getSupportedInputClasses() {
    return Set.of(Object.class);
  }

  @Override
  public <T> T parse(ObjectMapper objectMapper, Class<T> targetClass, String[] values) {
    return Stream.of(values)
        .flatMap(value -> Stream.of(value.split(",")))
        .map(value -> Filter.super.parse(objectMapper, targetClass, new String[] {value}))
        .collect((Collector<? super T, Object, T>) Collectors.toSet());
  }
}
