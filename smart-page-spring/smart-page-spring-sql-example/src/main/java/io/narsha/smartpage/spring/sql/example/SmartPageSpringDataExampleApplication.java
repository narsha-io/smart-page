package io.narsha.smartpage.spring.sql.example;

import io.narsha.smartpage.spring.core.configuration.FilterFactoryConfiguration;
import io.narsha.smartpage.spring.core.configuration.SmartPageResolverWebConfiguration;
import io.narsha.smartpage.spring.sql.configuration.JdbcFilterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
  JdbcFilterConfiguration.class,
  FilterFactoryConfiguration.class,
  SmartPageResolverWebConfiguration.class
})
public class SmartPageSpringDataExampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SmartPageSpringDataExampleApplication.class, args);
  }
}
