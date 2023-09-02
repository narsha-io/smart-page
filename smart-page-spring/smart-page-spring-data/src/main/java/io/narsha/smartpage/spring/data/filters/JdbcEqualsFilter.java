package io.narsha.smartpage.spring.data.filters;

import io.narsha.smartpage.core.filters.EqualsFilter;
import io.narsha.smartpage.core.filters.FilterParser;

public class JdbcEqualsFilter implements JdbcFilter<Object> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return EqualsFilter.class;
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s = :%s", property, property);
  }
}
