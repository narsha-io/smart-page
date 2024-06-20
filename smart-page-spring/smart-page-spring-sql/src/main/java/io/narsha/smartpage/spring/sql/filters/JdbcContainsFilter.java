package io.narsha.smartpage.spring.sql.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.filters.ContainsFilter;

/** JDBC filter for contains operation */
public class JdbcContainsFilter extends ContainsFilter implements JdbcFilter {

  /** default constructor */
  public JdbcContainsFilter() {}

  @Override
  public String getSQLFragment(String property) {
    return String.format("%s like :%s", property, property);
  }

  @Override
  public <T> T getParsedValue(ObjectMapper objectMapper, Class<T> targetClass, String[] values) {
    final var value = super.getParsedValue(objectMapper, targetClass, values);
    return (T) ("%" + value + "%");
  }
}
