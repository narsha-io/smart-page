package io.narsha.smartpage.core.filters;

import java.util.Set;

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
}
