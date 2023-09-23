package io.narsha.smartpage.spring.core.filters;

import io.narsha.smartpage.core.AbstractRegistrationService;
import io.narsha.smartpage.core.filters.FilterParser;
import java.util.Objects;
import java.util.Optional;

/**
 * Filter registration Service
 *
 * @param <T> Filter type
 */
public class AbstractFilterRegistrationService<T extends Filter>
    extends AbstractRegistrationService<T> {

  /**
   * Get a new instance of a filter
   *
   * @param filterParserType Filter type
   * @return instance of filter if found otherwise empty
   */
  public Optional<T> get(Class<? extends FilterParser> filterParserType) {
    return super.registeredService.stream()
        .filter(filter -> Objects.equals(filter.getParserType(), filterParserType))
        .findFirst();
  }
}
