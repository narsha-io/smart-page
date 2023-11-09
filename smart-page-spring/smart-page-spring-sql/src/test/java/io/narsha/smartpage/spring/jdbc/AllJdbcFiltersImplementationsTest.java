package io.narsha.smartpage.spring.jdbc;

import io.narsha.smartpage.spring.sql.filters.JdbcFilter;
import io.narsha.smartpage.web.AllFiltersImplementationsTest;

public class AllJdbcFiltersImplementationsTest extends AllFiltersImplementationsTest {

  public AllJdbcFiltersImplementationsTest() {
    super(JdbcFilter.class);
  }
}
