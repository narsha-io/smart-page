package io.narsha.smartpage.spring.sql.filters;

import io.narsha.smartpage.core.filters.FilterRegistrationService;

/** JdbcFilterRegistrationService that will register all JdbcFilter implementations */
public class JdbcFilterRegistrationService extends FilterRegistrationService<JdbcFilter> {

  /** default constructor */
  public JdbcFilterRegistrationService() {}
}
