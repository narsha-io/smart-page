package io.narsha.smartpage.core.sql.filters;

import io.narsha.smartpage.core.filters.Filter;

/** JDBC Filter interface */
public interface JdbcFilter extends Filter {

  /**
   * Generate a sql where clause for a defined property
   *
   * @param property the sql column name
   * @return SQL where clause
   */
  String getSQLFragment(String property);
}
