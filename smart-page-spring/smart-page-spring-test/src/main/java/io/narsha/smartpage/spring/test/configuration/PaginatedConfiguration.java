package io.narsha.smartpage.spring.test.configuration;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.spring.test.model.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class PaginatedConfiguration {

    @Bean
    public AtomicReference<PaginatedFilteredQuery<Person>> atomicReference() {
        return new AtomicReference<>();
    }
}
