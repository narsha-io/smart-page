package io.narsha.smartpage.spring.mongo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.spring.mongo.MongoQueryExecutor;
import io.narsha.smartpage.spring.mongo.SmartPageMongo;
import io.narsha.smartpage.spring.mongo.filters.MongoFilter;
import io.narsha.smartpage.spring.mongo.filters.MongoFilterRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/** Spring MongoFilter auto-registration configuration */
@Configuration
public class MongoFilterConfiguration
    extends AbstractFilterConfiguration<MongoFilter, MongoFilterRegistrationService> {

  /** default constructor */
  public MongoFilterConfiguration() {}

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
   * RowMapper that automatically map the query data into the targeted DTO
   *
   * @return rowMapper
   */
  @Bean
  public RowMapper rowMapper() {
    return new RowMapper(new ObjectMapper());
  }

  /**
   * Configure a mongoQueryExecutor in order to execute mongo query
   *
   * @param mongoTemplate mongo template in charge of the communication with mongo instance
   * @param mongoFilterRegistrationService auto registered mongo filters service
   * @param rowMapper conversion mapper
   * @return mongoQueryExecutor
   */
  @Bean
  public MongoQueryExecutor mongoQueryExecutor(
      MongoTemplate mongoTemplate,
      MongoFilterRegistrationService mongoFilterRegistrationService,
      RowMapper rowMapper) {
    return new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService, rowMapper);
  }

  /**
   * Create the mongo instance for smart page
   *
   * @param mongoQueryExecutor MongoQueryExecutor that will be in charge of execute the final mongo
   *     query
   * @return smart page mongo instance
   */
  @Bean
  public SmartPageMongo smartPageMongo(MongoQueryExecutor mongoQueryExecutor) {
    return new SmartPageMongo(mongoQueryExecutor);
  }
}
