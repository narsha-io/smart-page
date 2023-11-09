package io.narsha.smartpage.spring.sql.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.spring.sql.JdbcQueryExecutor;
import io.narsha.smartpage.spring.sql.SmartPageJdbc;
import io.narsha.smartpage.spring.sql.filters.JdbcFilter;
import io.narsha.smartpage.spring.sql.filters.JdbcFilterRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/** Spring jdbc filter configuration */
@Configuration
public class JdbcFilterConfiguration
    extends AbstractFilterConfiguration<JdbcFilter, JdbcFilterRegistrationService> {

  /**
   * Auto-register all JdbcFilter implementations
   *
   * @return the populated jdbcFilterRegistrationService with all JdbcFilter in the same package as
   *     JdbcFilter
   * @throws Exception if reflection Exception
   */
  @Bean
  public JdbcFilterRegistrationService jdbcFilterRegistrationService() throws Exception {
    return super.init(JdbcFilter.class);
  }

  /**
   * Init a JdbcQueryExecutor that will be in charge of execute the final SQL query
   *
   * @param namedParameterJdbcTemplate execute the final sql query
   * @param jdbcFilterRegistrationService will generate sql clause from DTO
   * @param rowMapper will convert the sql data as DTO
   * @return JdbcQueryExecutor
   */
  @Bean
  public JdbcQueryExecutor jdbcQueryExecutor(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      JdbcFilterRegistrationService jdbcFilterRegistrationService,
      RowMapper rowMapper) {
    return new JdbcQueryExecutor(
        namedParameterJdbcTemplate, jdbcFilterRegistrationService, rowMapper);
  }

  /**
   * RowMapper that automatically map the query data into the targeted DTO
   *
   * @return rowMapper
   */
  @Bean
  public RowMapper rowMapper() {
    return new RowMapper(new ObjectMapper());
  }

  /**
   * Create the jdbc instance for smart page
   *
   * @param jdbcQueryExecutor JdbcQueryExecutor that will be in charge of execute the final SQL
   *     query
   * @return smart page jdbc instance
   */
  @Bean
  public SmartPageJdbc smartPageJdbc(JdbcQueryExecutor jdbcQueryExecutor) {
    return new SmartPageJdbc(jdbcQueryExecutor);
  }
}
