package io.narsha.smartpage.core.filters;

/** Greater than filter factory */
public class GreaterThanOrEqualsFilterFactory implements FilterFactory {

  @Override
  public String getIdentifier() {
    return "gte";
  }

  @Override
  public GreaterThanOrEqualsFilter<?> get(Class<?> targetClass) {
    return new GreaterThanOrEqualsFilter<>(targetClass);
  }
}
