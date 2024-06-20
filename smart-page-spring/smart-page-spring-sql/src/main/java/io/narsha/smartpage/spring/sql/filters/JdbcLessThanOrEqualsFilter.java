package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.LessThanOrEqualsFilter;

/** JDBC filter for lte operation */
public class JdbcLessThanOrEqualsFilter extends LessThanOrEqualsFilter implements JdbcFilter {

  /** default constructor */
  public JdbcLessThanOrEqualsFilter() {}

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s <= :%s", property, property);
  }
}
