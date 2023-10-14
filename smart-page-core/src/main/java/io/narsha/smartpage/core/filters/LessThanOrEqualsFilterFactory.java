package io.narsha.smartpage.core.filters;

/** Greater than filter factory */
public class LessThanOrEqualsFilterFactory implements FilterFactory {

  @Override
  public String getIdentifier() {
    return "lte";
  }

  @Override
  public LessThanOrEqualsFilter<?> get(Class<?> targetClass) {
    return new LessThanOrEqualsFilter<>(targetClass);
  }
}
