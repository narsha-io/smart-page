package io.narsha.smartpage.spring.mongo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.spring.mongo.filters.MongoFilter;
import io.narsha.smartpage.spring.mongo.filters.MongoFilterRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Spring MongoFilter auto-registration configuration */
@Configuration
public class MongoFilterConfiguration
    extends AbstractFilterConfiguration<MongoFilter, MongoFilterRegistrationService> {

  /**
   * Auto-register all MongoFilter implementations
   *
   * @return the populated mongoFilterRegistrationService with all MongoFilter in the same package
   *     as MongoFilter
   * @throws Exception if reflection Exception
   */
  @Bean
  public MongoFilterRegistrationService jdbcFilterRegistrationService() throws Exception {
    return super.init(MongoFilter.class);
  }

  /**
   * RowMapper that automatically map the query result into the targeted DTO
   *
   * @return rowMapper
   */
  @Bean
  public RowMapper rowMapper() {
    return new RowMapper(new ObjectMapper());
  }
}
