package io.narsha.smartpage.core.filters;

/** Greater than filter factory */
public class LessThanFilterFactory implements FilterFactory {

  @Override
  public String getIdentifier() {
    return "lt";
  }

  @Override
  public LessThanFilter<?> get(Class<?> targetClass) {
    return new LessThanFilter<>(targetClass);
  }
}
