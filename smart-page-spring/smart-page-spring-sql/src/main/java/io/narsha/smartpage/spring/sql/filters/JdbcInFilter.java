package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.filters.InFilter;
import java.util.Set;

/** JDBC filter for in operation */
public class JdbcInFilter implements JdbcFilter<Set<Object>> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return InFilter.class;
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s in (:%s)", property, property);
  }
}
