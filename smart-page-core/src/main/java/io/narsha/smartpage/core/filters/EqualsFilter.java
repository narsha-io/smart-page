package io.narsha.smartpage.core.filters;

import java.util.Set;

/**
 * class which provide some method to parse String array into a targetClass in order to apply an
 * Equals than operation
 */
public class EqualsFilter implements Filter {
  @Override
  public String getFilterAlias() {
    return "equals";
  }

  @Override
  public Set<Class<?>> getSupportedInputClasses() {
    return Set.of(Object.class);
  }
}
