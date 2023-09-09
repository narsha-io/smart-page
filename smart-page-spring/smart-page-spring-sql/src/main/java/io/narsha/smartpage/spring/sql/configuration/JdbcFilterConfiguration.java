package io.narsha.smartpage.spring.sql.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.spring.sql.JdbcQueryExecutor;
import io.narsha.smartpage.spring.sql.filters.JdbcFilter;
import io.narsha.smartpage.spring.sql.filters.JdbcFilterRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class JdbcFilterConfiguration
    extends AbstractFilterConfiguration<JdbcFilter, JdbcFilterRegistrationService> {

  @Bean
  public JdbcFilterRegistrationService jdbcFilterRegistrationService() throws Exception {
    return super.init(JdbcFilter.class);
  }

  @Bean
  public JdbcQueryExecutor jdbcQueryExecutor(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      JdbcFilterRegistrationService jdbcFilterRegistrationService,
      RowMapper rowMapper) {
    return new JdbcQueryExecutor(
        namedParameterJdbcTemplate, jdbcFilterRegistrationService, rowMapper);
  }

  @Bean
  public RowMapper rowMapper() {
    return new RowMapper(new ObjectMapper());
  }
}
