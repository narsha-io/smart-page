package io.narsha.smartpage.core.sql.filters;

import io.narsha.smartpage.core.filters.EqualsFilter;

/** JDBC filter for equals operation */
public class JdbcEqualsFilter extends EqualsFilter implements JdbcFilter {

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s = :%s", property, property);
  }
}
