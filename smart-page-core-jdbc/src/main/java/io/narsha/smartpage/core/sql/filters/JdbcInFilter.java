package io.narsha.smartpage.core.sql.filters;

import io.narsha.smartpage.core.filters.InFilter;

/** JDBC filter for in operation */
public class JdbcInFilter extends InFilter implements JdbcFilter {

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s in (:%s)", property, property);
  }
}
