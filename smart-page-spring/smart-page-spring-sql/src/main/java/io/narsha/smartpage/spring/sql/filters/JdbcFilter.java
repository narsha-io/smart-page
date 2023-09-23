package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.spring.core.filters.Filter;

/**
 * JDBC Filter interface
 *
 * @param <T> target parsed type
 */
public interface JdbcFilter<T> extends Filter<T> {

  /**
   * Generate a sql where clause for a defined property
   *
   * @param property the sql column name
   * @return SQL where clause
   */
  String getSQLFragment(String property);
}
