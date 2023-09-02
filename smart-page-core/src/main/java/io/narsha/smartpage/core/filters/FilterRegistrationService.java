package io.narsha.smartpage.core.filters;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class FilterRegistrationService {

  private final Set<FilterFactory> filters = new HashSet<>();

  public void register(FilterFactory filter) {
    this.filters.add(filter);
  }

  public Optional<? extends FilterParser<?, ?>> get(Class<?> targetClass, String identifier) {
    return filters.stream()
        .filter(
            filterFactory ->
                Objects.equals(
                    filterFactory.getIdentifier().toLowerCase(), identifier.toLowerCase()))
        .map(filterFactory -> filterFactory.get(targetClass))
        .findFirst();
  }
}
