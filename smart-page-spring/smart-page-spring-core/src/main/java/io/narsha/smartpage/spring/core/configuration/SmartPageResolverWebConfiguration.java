package io.narsha.smartpage.spring.core.configuration;

import io.narsha.smartpage.spring.core.web.SmartPageQueryResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Register PaginatedFilteredQueryResolver into Spring argumentResolver */
@Configuration
@RequiredArgsConstructor
public class SmartPageResolverWebConfiguration implements WebMvcConfigurer {

  private final SmartPageQueryResolver smartPageQueryResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(smartPageQueryResolver);
  }
}
