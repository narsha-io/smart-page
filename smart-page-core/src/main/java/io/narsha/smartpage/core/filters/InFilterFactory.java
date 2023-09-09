package io.narsha.smartpage.core.filters;

public class InFilterFactory implements FilterFactory {

  @Override
  public String getIdentifier() {
    return "in";
  }

  @Override
  public InFilter<?> get(Class<?> targetClass) {
    return new InFilter<>(targetClass);
  }
}
