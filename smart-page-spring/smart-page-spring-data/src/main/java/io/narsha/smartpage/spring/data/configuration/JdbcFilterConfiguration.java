package io.narsha.smartpage.spring.data.configuration;

import static org.reflections.scanners.Scanners.SubTypes;

import io.narsha.smartpage.spring.data.filters.JdbcFilter;
import io.narsha.smartpage.spring.data.filters.JdbcFilterRegistrationService;
import java.util.Set;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcFilterConfiguration {

  @Bean
  public JdbcFilterRegistrationService jdbcFilterRegistrationService() {
    final var internalFilters = findInternalJdbcFilterFactories();
    final var res = new JdbcFilterRegistrationService();

    try {
      for (var filterFactory : internalFilters) {
        final var factoryInstance = (JdbcFilter) filterFactory.getConstructor().newInstance();
        res.register(factoryInstance);
      }
    } catch (Exception e) {
      // TODO throw exception
    }

    return res;
  }

  private Set<Class<?>> findInternalJdbcFilterFactories() {
    Reflections reflections = new Reflections("io.narsha.smartpage.spring.data.filters");
    return reflections.get(SubTypes.of(JdbcFilter.class).asClass());
  }
}
