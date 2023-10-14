package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.filters.LessThanFilter;

/** JDBC filter for in operation */
public class JdbcLessThanFilter implements JdbcFilter<Object> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return LessThanFilter.class;
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s < :%s", property, property);
  }
}
