package io.narsha.smartpage.core.filters;

import io.narsha.smartpage.core.AbstractRegistrationService;
import java.util.Objects;
import java.util.Optional;

public class FilterFactoryRegistrationService extends AbstractRegistrationService<FilterFactory> {

  public Optional<? extends FilterParser<?, ?>> get(Class<?> targetClass, String identifier) {
    return super.registeredService.stream()
        .filter(
            filterFactory ->
                Objects.equals(
                    filterFactory.getIdentifier().toLowerCase(), identifier.toLowerCase()))
        .map(filterFactory -> filterFactory.get(targetClass))
        .findFirst();
  }
}
