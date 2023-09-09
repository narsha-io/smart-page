package io.narsha.smartpage.core.filters;

public class EqualsFilterFactory implements FilterFactory {

  @Override
  public String getIdentifier() {
    return "equals";
  }

  @Override
  public EqualsFilter<?> get(Class<?> targetClass) {
    return new EqualsFilter<>(targetClass);
  }
}
