package io.narsha.smartpage.core.filters;

public class InFilterFactory implements FilterFactory {

  @Override
  public String getIdentifier() {
    return "in";
  }

  @Override
  public <R> InFilter<R> get(Class<R> targetClass) {
    return new InFilter<>(targetClass);
  }
}
