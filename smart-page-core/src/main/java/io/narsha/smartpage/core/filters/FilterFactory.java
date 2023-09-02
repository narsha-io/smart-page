package io.narsha.smartpage.core.filters;

public interface FilterFactory {

  String getIdentifier();

  <R> FilterParser<?, ?> get(Class<R> targetClass);
}
