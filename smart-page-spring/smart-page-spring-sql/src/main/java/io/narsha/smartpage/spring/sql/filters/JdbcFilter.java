package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.spring.core.filters.Filter;

public interface JdbcFilter<T> extends Filter<T> {

  String getSQLFragment(String property);
}
