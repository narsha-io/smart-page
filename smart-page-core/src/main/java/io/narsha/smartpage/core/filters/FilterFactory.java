package io.narsha.smartpage.core.filters;

/** Abstract factory with basic method to detect filter type and get a new instance */
public interface FilterFactory {

  /**
   * Unique filter identifier. basically the filter name like equals,contains ...
   *
   * @return the identifier
   */
  String getIdentifier();

  /**
   * Get a new filterParser instance
   *
   * @param targetClass Class of the target property
   * @return a filterParser corresponding to the targetClass
   */
  FilterParser<?, ?> get(Class<?> targetClass);
}
