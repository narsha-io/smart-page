package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.LessThanFilter;

/** JDBC filter for lt operation */
public class JdbcLessThanFilter extends LessThanFilter implements JdbcFilter {

  /** default constructor */
  public JdbcLessThanFilter() {}

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s < :%s", property, property);
  }
}
