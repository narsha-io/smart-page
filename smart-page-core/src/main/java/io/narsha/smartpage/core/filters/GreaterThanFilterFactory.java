package io.narsha.smartpage.core.filters;

/** Greater than filter factory */
public class GreaterThanFilterFactory implements FilterFactory {

  @Override
  public String getIdentifier() {
    return "gt";
  }

  @Override
  public GreaterThanFilter<?> get(Class<?> targetClass) {
    return new GreaterThanFilter<>(targetClass);
  }
}
