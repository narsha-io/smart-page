package io.narsha.smartpage.core.filters;

public class ContainsFilterFactory implements FilterFactory {

  @Override
  public String getIdentifier() {
    return "contains";
  }

  @Override
  public <R> ContainsFilter get(Class<R> targetClass) {
    if (!targetClass.equals(String.class)) {
      throw new IllegalArgumentException();
      // TODO custom exception
    }
    return new ContainsFilter();
  }
}
