package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.filters.GreaterThanFilter;

/** JDBC filter for in operation */
public class JdbcGreaterThanFilter implements JdbcFilter<Object> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return GreaterThanFilter.class;
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s > :%s", property, property);
  }
}
