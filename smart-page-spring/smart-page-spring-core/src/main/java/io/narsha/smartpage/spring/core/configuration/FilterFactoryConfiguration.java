package io.narsha.smartpage.spring.core.configuration;

import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.core.filters.FilterFactory;
import io.narsha.smartpage.core.filters.FilterFactoryRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterFactoryConfiguration
    extends AbstractFilterConfiguration<FilterFactory, FilterFactoryRegistrationService> {

  @Bean
  public FilterFactoryRegistrationService filterRegistrationService() throws Exception {
    return super.init(FilterFactory.class);
  }
}
