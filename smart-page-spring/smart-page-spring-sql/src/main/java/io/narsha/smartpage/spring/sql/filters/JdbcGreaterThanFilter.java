package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.GreaterThanFilter;

/** JDBC filter for > operation */
public class JdbcGreaterThanFilter extends GreaterThanFilter implements JdbcFilter {

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s > :%s", property, property);
  }
}
