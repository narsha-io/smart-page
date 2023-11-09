package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

/**
 * class which provide some method to parse String array into a targetClass in order to apply an
 * Contains than operation
 */
public class ContainsFilter implements Filter {
  @Override
  public String getFilterAlias() {
    return "contains";
  }

  @Override
  public Set<Class<?>> getSupportedInputClasses() {
    return Set.of(String.class);
  }

  @Override
  public <T> T parse(ObjectMapper objectMapper, Class<T> targetClass, String[] values) {
    var parsed =
        Stream.of(values)
            .filter(Objects::nonNull)
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.joining(","));

    return objectMapper.convertValue(parsed, targetClass);
  }

  @Override
  public boolean isMultiValueSupported() {
    return true;
  }
}
