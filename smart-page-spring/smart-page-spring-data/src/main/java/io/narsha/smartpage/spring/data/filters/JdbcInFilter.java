package io.narsha.smartpage.spring.data.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.filters.InFilter;

public class JdbcInFilter implements JdbcFilter {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return InFilter.class;
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s in (:%s)", property, property);
  }
}
