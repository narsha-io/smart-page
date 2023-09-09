package io.narsha.smartpage.spring.mongo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.spring.mongo.filters.MongoFilter;
import io.narsha.smartpage.spring.mongo.filters.MongoFilterRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoFilterConfiguration
    extends AbstractFilterConfiguration<MongoFilter, MongoFilterRegistrationService> {

  @Bean
  public MongoFilterRegistrationService jdbcFilterRegistrationService() throws Exception {
    return super.init(MongoFilter.class);
  }

  @Bean
  public RowMapper rowMapper() {
    return new RowMapper(new ObjectMapper());
  }
}
