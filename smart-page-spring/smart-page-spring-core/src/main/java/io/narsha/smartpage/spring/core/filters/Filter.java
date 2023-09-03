package io.narsha.smartpage.spring.core.filters;

import io.narsha.smartpage.core.filters.FilterParser;

public interface Filter<T> {

  Class<? extends FilterParser> getParserType();

  default T getValue(T object) {
    return object;
  }
}
