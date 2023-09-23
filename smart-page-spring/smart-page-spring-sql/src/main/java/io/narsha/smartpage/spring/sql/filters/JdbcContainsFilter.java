package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.ContainsFilter;
import io.narsha.smartpage.core.filters.FilterParser;

/** JDBC filter for contains operation */
public class JdbcContainsFilter implements JdbcFilter<String> {

  @Override
  public Class<? extends FilterParser<?, ?>> getParserType() {
    return ContainsFilter.class;
  }

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s like :%s", property, property);
  }

  @Override
  public String getParsedValue(String value) {
    return "%" + value + "%";
  }
}
