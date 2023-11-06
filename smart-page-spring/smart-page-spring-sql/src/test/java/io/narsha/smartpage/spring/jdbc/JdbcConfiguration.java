package io.narsha.smartpage.spring.jdbc;

import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.core.filters.FilterRegistrationService;
import io.narsha.smartpage.spring.sql.filters.JdbcFilter;
import io.narsha.smartpage.spring.sql.filters.JdbcFilterRegistrationService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class JdbcConfiguration
    extends AbstractFilterConfiguration<JdbcFilter, JdbcFilterRegistrationService> {

  @Bean
  @Primary
  public FilterRegistrationService<JdbcFilter> testFilterRegistrationService() throws Exception {
    return super.init(JdbcFilter.class);
  }
}
