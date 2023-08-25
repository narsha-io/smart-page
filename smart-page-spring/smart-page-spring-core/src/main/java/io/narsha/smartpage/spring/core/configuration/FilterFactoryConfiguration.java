package io.narsha.smartpage.spring.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.core.filters.FilterFactory;
import io.narsha.smartpage.core.filters.FilterFactoryRegistrationService;
import io.narsha.smartpage.spring.core.web.PaginatedFilteredQueryResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

@Configuration
public class FilterFactoryConfiguration
    extends AbstractFilterConfiguration<FilterFactory, FilterFactoryRegistrationService> {

  @Bean
  public FilterFactoryRegistrationService filterRegistrationService() throws Exception {
    return super.init(FilterFactory.class);
  }

  @Bean
  public PaginatedFilteredQueryResolver paginatedFilteredQueryResolver(
      ObjectMapper objectMapper,
      PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver,
      FilterFactoryRegistrationService filterFactoryRegistrationService) {
    return new PaginatedFilteredQueryResolver(
        objectMapper, pageableHandlerMethodArgumentResolver, filterFactoryRegistrationService);
  }
}
