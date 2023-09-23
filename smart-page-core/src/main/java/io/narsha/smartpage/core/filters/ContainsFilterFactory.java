package io.narsha.smartpage.core.filters;

/** A factory which create a new instance of contains filter */
public class ContainsFilterFactory implements FilterFactory {

  @Override
  public String getIdentifier() {
    return "contains";
  }

  @Override
  public ContainsFilter get(Class targetClass) {
    if (!targetClass.equals(String.class)) {
      throw new IllegalArgumentException();
      // TODO custom exception
    }
    return new ContainsFilter();
  }
}
