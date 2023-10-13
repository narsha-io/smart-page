package io.narsha.smartpage.spring.test.configuration;

import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.spring.test.model.Person;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaginatedConfiguration {

  @Bean
  public AtomicReference<SmartPageQuery<Person>> atomicReference() {
    return new AtomicReference<>();
  }
}
