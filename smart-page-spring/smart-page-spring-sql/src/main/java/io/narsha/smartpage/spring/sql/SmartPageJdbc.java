package io.narsha.smartpage.spring.sql;

import io.narsha.smartpage.spring.core.SmartPage;
import java.util.Map;

/** spring jdbc implementation */
public class SmartPageJdbc extends SmartPage<Map<String, Object>> {

  /**
   * constructor
   *
   * @param jdbcQueryExecutor jdbcQueryExecutor
   */
  public SmartPageJdbc(JdbcQueryExecutor jdbcQueryExecutor) {
    super(jdbcQueryExecutor);
  }
}
