package io.narsha.smartpage.spring.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.filters.FilterRegistrationService;
import io.narsha.smartpage.spring.core.SmartPage;
import io.narsha.smartpage.spring.core.web.SmartPageQueryResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

/**
 * Spring Configuration which register FilterFactoryRegistrationService /
 * PaginatedFilteredQueryResolver bean
 */
@Configuration
public class FilterFactoryConfiguration {

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
  public SmartPageQueryResolver paginatedFilteredQueryResolver(
      ObjectMapper objectMapper,
      PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver,
      FilterRegistrationService filterFactoryRegistrationService) {
    return new SmartPageQueryResolver(
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
