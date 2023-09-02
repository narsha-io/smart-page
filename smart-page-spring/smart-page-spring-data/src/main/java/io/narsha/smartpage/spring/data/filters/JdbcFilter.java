package io.narsha.smartpage.spring.data.filters;

import io.narsha.smartpage.core.filters.FilterParser;

public interface JdbcFilter {

  Class<? extends FilterParser> getParserType();

  String getSQLFragment(String property);
}
