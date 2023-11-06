package io.narsha.smartpage.core.filters;

import io.narsha.smartpage.core.AbstractRegistrationService;
import java.util.Objects;
import java.util.Optional;

/** Filter registration Service */
public class FilterRegistrationService<T extends Filter> extends AbstractRegistrationService<T> {

  /**
   * Get a new instance of a filter
   *
   * @param alias filter alias
   * @return instance of filter if found otherwise empty
   */
  public Optional<T> get(String alias) {
    return super.registeredService.stream()
        .filter(filter -> Objects.equals(filter.getFilterAlias(), alias))
        .findFirst();
  }
}
