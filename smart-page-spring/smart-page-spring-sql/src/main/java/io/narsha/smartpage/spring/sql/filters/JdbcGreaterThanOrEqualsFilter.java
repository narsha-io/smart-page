package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.GreaterThanOrEqualsFilter;

/** JDBC filter for >= operation */
public class JdbcGreaterThanOrEqualsFilter extends GreaterThanOrEqualsFilter implements JdbcFilter {

  /** default constructor */
  public JdbcGreaterThanOrEqualsFilter() {}

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s >= :%s", property, property);
  }
}
