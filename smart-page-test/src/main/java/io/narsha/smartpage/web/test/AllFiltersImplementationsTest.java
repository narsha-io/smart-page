package io.narsha.smartpage.web.test;

import static org.junit.jupiter.api.Assertions.fail;

import io.narsha.smartpage.core.filters.Filter;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.function.Failable;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

@Slf4j
public abstract class AllFiltersImplementationsTest {

  private final Set<Class<?>> parsers;
  private final Class<? extends Filter> filterClass;

  protected AllFiltersImplementationsTest(Class<? extends Filter> filterClass) {
    final var reflections = new Reflections(Filter.class.getPackageName());
    this.parsers = reflections.get(Scanners.SubTypes.of(Filter.class).asClass());
    log.info(
        "{} parsers found {}",
        this.parsers.size(),
        this.parsers.stream().map(Class::getSimpleName).toList());
    this.filterClass = filterClass;
  }

  @Test
  void checkFiltersImplementation() {
    final var reflections = new Reflections(this.filterClass.getPackageName());
    final var filters = reflections.get(Scanners.SubTypes.of(this.filterClass).asClass());
    log.info(
        "{} filters found {}", filters.size(), filters.stream().map(Class::getSimpleName).toList());

    var parserType =
        filters.stream()
            .map(
                Failable.asFunction(
                    e -> {
                      var constructor = e.getConstructor();
                      return (Filter) constructor.newInstance();
                    }))
            .map(Filter::getFilterAlias)
            .collect(Collectors.toSet());

    this.parsers.removeIf(parserType::contains);

    if (!this.parsers.isEmpty()) {
      fail(String.format("Cannot find filter implementation for parser : %s", this.parsers));
    }
  }
}
