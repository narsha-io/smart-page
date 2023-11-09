package io.narsha.smartpage.spring.sql;

import io.narsha.smartpage.spring.core.SmartPage;
import java.util.Map;

public class SmartPageJdbc extends SmartPage<Map<String, Object>> {
  public SmartPageJdbc(JdbcQueryExecutor jdbcQueryExecutor) {
    super(jdbcQueryExecutor);
  }
}
