package io.narsha.smartpage.spring.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.core.filters.FilterFactory;
import io.narsha.smartpage.core.filters.FilterFactoryRegistrationService;
import io.narsha.smartpage.spring.core.web.PaginatedFilteredQueryResolver;
import io.narsha.smartpage.spring.core.web.utils.SmartPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

/**
 * Spring Configuration which register FilterFactoryRegistrationService /
 * PaginatedFilteredQueryResolver bean
 */
@Configuration
public class FilterFactoryConfiguration
    extends AbstractFilterConfiguration<FilterFactory, FilterFactoryRegistrationService> {

  /**
   * Register an initiated FilterFactoryRegistrationService with internal declared Filter
   *
   * @return FilterFactoryRegistrationService bean
   * @throws Exception init reflection exception
   */
  @Bean
  public FilterFactoryRegistrationService filterRegistrationService() throws Exception {
    return super.init(FilterFactory.class);
  }

  /**
   * Register a PaginatedFilteredQueryResolver which convert a http request into
   * PaginatedFilteredQuery
   *
   * @param objectMapper Jackson objectMapper to convert object
   * @param pageableHandlerMethodArgumentResolver get pageable from http request
   * @param filterFactoryRegistrationService use to resolve http parameter into Filter
   * @return PaginatedFilteredQueryResolver that will convert a http request into
   *     PaginatedFilteredQuery
   */
  @Bean
  public PaginatedFilteredQueryResolver paginatedFilteredQueryResolver(
      ObjectMapper objectMapper,
      PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver,
      FilterFactoryRegistrationService filterFactoryRegistrationService) {
    return new PaginatedFilteredQueryResolver(
        objectMapper, pageableHandlerMethodArgumentResolver, filterFactoryRegistrationService);
  }

  /**
   * Register the smartPage entry point
   *
   * @param queryExecutor in charge of executing the query
   * @return smartPage bean
   */
  @Bean
  public SmartPage smartPage(QueryExecutor queryExecutor) {
    return new SmartPage(queryExecutor);
  }
}
