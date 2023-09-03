package io.narsha.smartpage.spring.data.filters;

import io.narsha.smartpage.core.filters.FilterParser;

public interface JdbcFilter<T> {

  Class<? extends FilterParser> getParserType();

  String getSQLFragment(String property);

  default T getValue(T object) {
    return object;
  }
}
