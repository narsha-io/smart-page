package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class FilterParser<T, R> {

  protected Class<T> targetClass;

  public FilterParser(Class<T> targetClass) {
    this.targetClass = targetClass;
  }

  public abstract void parse(ObjectMapper objectMapper, String[] value);

  public abstract R getValue();
}
