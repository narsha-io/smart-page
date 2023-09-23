package io.narsha.smartpage.core.filters;

import io.narsha.smartpage.core.AbstractRegistrationService;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/** Service that will register all filter and instantiate them following a unique identifier */
public class FilterFactoryRegistrationService extends AbstractRegistrationService<FilterFactory> {

  /**
   * Find a FilterParser for a targetClass that match the identifier parameter
   *
   * @param targetClass the DTO attribute class
   * @param identifier the unique filter identifier
   * @return a filterParser that match de both parameter of Optional.empty
   */
  public Optional<FilterParser> get(Class<?> targetClass, String identifier) {
    final Stream<FilterParser> filterStream =
        super.registeredService.stream()
            .filter(
                filterFactory ->
                    Objects.equals(
                        filterFactory.getIdentifier().toLowerCase(), identifier.toLowerCase()))
            .map(filterFactory -> filterFactory.get(targetClass));

    return filterStream.findFirst();
  }
}
