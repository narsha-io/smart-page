package io.narsha.smartpage.spring.core.configuration;

import static org.reflections.scanners.Scanners.SubTypes;

import io.narsha.smartpage.core.filters.FilterFactory;
import io.narsha.smartpage.core.filters.FilterRegistrationService;
import java.util.Set;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

  @Bean
  public FilterRegistrationService filterRegistrationService() {
    final var internalFilters = findInternalFilterFactories();
    final var res = new FilterRegistrationService();

    try {
      for (var filterFactory : internalFilters) {
        final var factoryInstance = (FilterFactory) filterFactory.getConstructor().newInstance();
        res.register(factoryInstance);
      }
    } catch (Exception e) {
      // TODO throw exception
    }

    return res;
  }

  private Set<Class<?>> findInternalFilterFactories() {
    Reflections reflections = new Reflections("io.narsha.smartpage.core.filters");
    return reflections.get(SubTypes.of(FilterFactory.class).asClass());
  }
}
