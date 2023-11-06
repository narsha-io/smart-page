package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.LessThanFilter;

/** JDBC filter for < operation */
public class JdbcLessThanFilter extends LessThanFilter implements JdbcFilter {
  @Override
  public String getSQLFragment(String property) {
    return String.format("%s < :%s", property, property);
  }
}
