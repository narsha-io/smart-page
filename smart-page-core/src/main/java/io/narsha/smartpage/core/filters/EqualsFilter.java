package io.narsha.smartpage.core.filters;

import java.util.Set;

public class EqualsFilter extends Filter {
  @Override
  public String getFilterAlias() {
    return "equals";
  }

  @Override
  public Set<Class<?>> getSupportedInputClasses() {
    return Set.of(Object.class);
  }
}
