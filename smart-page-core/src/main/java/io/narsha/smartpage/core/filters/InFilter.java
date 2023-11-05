package io.narsha.smartpage.core.filters;

import java.util.Set;

/**
 * class which provide some method to parse String array into a targetClass in order to apply an
 * Greater than operation
 */
public class InFilter extends Filter {

  @Override
  public String getFilterAlias() {
    return "in";
  }

  @Override
  protected Set<Class<?>> getSupportedInputClasses() {
    return Set.of(Object.class);
  }
}
