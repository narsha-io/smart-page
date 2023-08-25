package io.narsha.smartpage.spring.core.configuration;

import io.narsha.smartpage.spring.core.web.PaginatedFilteredQueryResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SmartPageResolverWebConfiguration implements WebMvcConfigurer {

  private final PaginatedFilteredQueryResolver paginatedFilteredQueryResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(paginatedFilteredQueryResolver);
  }
}
