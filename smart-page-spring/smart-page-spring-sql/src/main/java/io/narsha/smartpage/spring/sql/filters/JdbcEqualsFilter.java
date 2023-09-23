package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.EqualsFilter;
import io.narsha.smartpage.core.filters.FilterParser;

/** JDBC filter for equals operation */
public class JdbcEqualsFilter implements JdbcFilter<Object> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return EqualsFilter.class;
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s = :%s", property, property);
  }
}
