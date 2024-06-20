package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.EqualsFilter;

/** JDBC filter for equals operation */
public class JdbcEqualsFilter extends EqualsFilter implements JdbcFilter {

  /** default constructor */
  public JdbcEqualsFilter() {}

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s = :%s", property, property);
  }
}
