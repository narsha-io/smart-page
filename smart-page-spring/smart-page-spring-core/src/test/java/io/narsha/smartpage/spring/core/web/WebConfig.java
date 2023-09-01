package io.narsha.smartpage.spring.core.web;

import io.narsha.smartpage.core.exceptions.UnknownFilterException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
public class WebConfig implements WebMvcConfigurer {

  @Autowired private PaginatedFilteredQueryResolver paginatedFilteredQueryResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(paginatedFilteredQueryResolver);
  }

  @ControllerAdvice
  public class PaginatedFilterControllerAdvice {

    @ExceptionHandler(UnknownFilterException.class)
    protected ResponseEntity<String> handleHttpRequestMethodNotSupportedException(
        UnknownFilterException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
