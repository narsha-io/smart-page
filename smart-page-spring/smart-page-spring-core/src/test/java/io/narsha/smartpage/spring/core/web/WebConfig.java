package io.narsha.smartpage.spring.core.web;

import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.core.SmartPageResult;
import io.narsha.smartpage.core.configuration.AbstractFilterConfiguration;
import io.narsha.smartpage.core.exceptions.InternalException;
import io.narsha.smartpage.core.filters.Filter;
import io.narsha.smartpage.core.filters.FilterRegistrationService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@TestConfiguration
public class WebConfig
    extends AbstractFilterConfiguration<Filter, FilterRegistrationService<Filter>> {

  @ControllerAdvice
  public class PaginatedFilterControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class, InternalException.class})
    protected ResponseEntity<String> handleHttpRequestMethodInvalidException(Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @Bean
  public QueryExecutor queryExecutor() {
    return new QueryExecutor() {
      @Override
      public <T> SmartPageResult<T> execute(SmartPageQuery<T> paginatedFilteredQuery) {
        return null;
      }
    };
  }

  /**
   * Register an initiated FilterFactoryRegistrationService with internal declared Filter
   *
   * @return FilterFactoryRegistrationService bean
   * @throws Exception init reflection exception
   */
  @Bean
  public FilterRegistrationService filterRegistrationService() throws Exception {
    return super.init(Filter.class);
  }
}
