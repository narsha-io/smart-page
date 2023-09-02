package io.narsha.smartpage.spring.data.filters;

import io.narsha.smartpage.core.filters.ContainsFilter;
import io.narsha.smartpage.core.filters.FilterParser;

public class JdbcContainsFilter implements JdbcFilter {

  @Override
  public Class<? extends FilterParser<?, ?>> getParserType() {
    return ContainsFilter.class;
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s like :%s", property, property);
  }
}
