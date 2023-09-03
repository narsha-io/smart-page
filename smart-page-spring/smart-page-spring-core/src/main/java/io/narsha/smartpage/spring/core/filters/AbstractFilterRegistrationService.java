package io.narsha.smartpage.spring.core.filters;

import io.narsha.smartpage.core.AbstractRegistrationService;
import io.narsha.smartpage.core.filters.FilterParser;
import java.util.Objects;
import java.util.Optional;

public class AbstractFilterRegistrationService<T extends Filter>
    extends AbstractRegistrationService<T> {

  public Optional<T> get(Class<? extends FilterParser> filterParserType) {
    return super.registeredService.stream()
        .filter(filter -> Objects.equals(filter.getParserType(), filterParserType))
        .findFirst();
  }
}
