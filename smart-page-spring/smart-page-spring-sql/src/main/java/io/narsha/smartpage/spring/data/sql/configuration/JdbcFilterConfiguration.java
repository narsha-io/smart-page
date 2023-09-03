package io.narsha.smartpage.spring.data.sql.configuration;


import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.spring.data.sql.filters.JdbcFilter;
import io.narsha.smartpage.spring.data.sql.filters.JdbcFilterRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcFilterConfiguration
    extends AbstractFilterConfiguration<JdbcFilter, JdbcFilterRegistrationService> {

  @Bean
  public JdbcFilterRegistrationService jdbcFilterRegistrationService() throws Exception {
    return super.init(JdbcFilter.class);
  }
}
