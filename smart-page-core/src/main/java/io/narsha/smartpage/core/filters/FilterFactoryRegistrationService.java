package io.narsha.smartpage.core.filters;

import io.narsha.smartpage.core.AbstractRegistrationService;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class FilterFactoryRegistrationService extends AbstractRegistrationService<FilterFactory> {

  public Optional<FilterParser<?, ?>> get(Class<?> targetClass, String identifier) {
    final Stream<FilterParser<?, ?>> filterStream =
        super.registeredService.stream()
            .filter(
                filterFactory ->
                    Objects.equals(
                        filterFactory.getIdentifier().toLowerCase(), identifier.toLowerCase()))
            .map(filterFactory -> filterFactory.get(targetClass));

    return filterStream.findFirst();
  }
}
