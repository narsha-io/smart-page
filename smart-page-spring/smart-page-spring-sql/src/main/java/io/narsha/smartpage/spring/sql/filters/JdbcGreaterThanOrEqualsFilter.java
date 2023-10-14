package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.filters.GreaterThanOrEqualsFilter;

/** JDBC filter for in operation */
public class JdbcGreaterThanOrEqualsFilter implements JdbcFilter<Object> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return GreaterThanOrEqualsFilter.class;
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s >= :%s", property, property);
  }
}
