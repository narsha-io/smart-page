package io.narsha.smartpage.core.filters;

/**
 * Filter interface
 *
 * @param <T> target parsed type
 */
public interface Filter<T> {

  /**
   * Class of associated filter parser
   *
   * @return FilterParser class
   */
  Class<? extends FilterParser> getParserType();

  /**
   * Parse the object parameter into the target class
   *
   * @param object the object to parse
   * @return the parsed value
   */
  default T getParsedValue(T object) {
    return object;
  }
}
