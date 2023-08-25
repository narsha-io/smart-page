package io.narsha.smartpage.core.filters;

public interface FilterFactory {

  String getIdentifier();

  FilterParser<?, ?> get(Class<?> targetClass);
}
