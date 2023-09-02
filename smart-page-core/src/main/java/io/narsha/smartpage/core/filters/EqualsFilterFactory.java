package io.narsha.smartpage.core.filters;

public class EqualsFilterFactory implements FilterFactory {

  @Override
  public String getIdentifier() {
    return "equals";
  }

  @Override
  public <R> EqualsFilter<R> get(Class<R> targetClass) {
    return new EqualsFilter<>(targetClass);
  }
}
