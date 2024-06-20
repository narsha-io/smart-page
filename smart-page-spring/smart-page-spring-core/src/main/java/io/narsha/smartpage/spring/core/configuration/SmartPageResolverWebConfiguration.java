package io.narsha.smartpage.spring.core.configuration;

import io.narsha.smartpage.spring.core.web.SmartPageQueryResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Register PaginatedFilteredQueryResolver into Spring argumentResolver */
@Configuration
public class SmartPageResolverWebConfiguration implements WebMvcConfigurer {

  private final SmartPageQueryResolver smartPageQueryResolver;

  /**
   * constructor
   *
   * @param smartPageQueryResolver smartPageQueryResolver
   */
  public SmartPageResolverWebConfiguration(SmartPageQueryResolver smartPageQueryResolver) {
    this.smartPageQueryResolver = smartPageQueryResolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(smartPageQueryResolver);
  }
}
