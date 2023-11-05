package io.narsha.smartpage.core.filters;

import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Set;

/**
 * class which provide some method to parse String array into a targetClass in order to apply an
 * Greater than operation
 */
public class GreaterThanOrEqualsFilter extends Filter {

  @Override
  public String getFilterAlias() {
    return "gte";
  }

  @Override
  protected Set<Class<?>> getSupportedInputClasses() {
    return Set.of(String.class, Number.class, Date.class, Temporal.class);
  }
}
