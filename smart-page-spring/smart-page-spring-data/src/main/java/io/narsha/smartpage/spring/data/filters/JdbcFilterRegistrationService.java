package io.narsha.smartpage.spring.data.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class JdbcFilterRegistrationService {

  private final Set<JdbcFilter> filters = new HashSet<>();

  public void register(JdbcFilter filter) {
    this.filters.add(filter);
  }

  public Optional<JdbcFilter> get(Class<? extends FilterParser> filterParserType) {
    return filters.stream()
        .filter(filter -> Objects.equals(filter.getParserType(), filterParserType))
        .findFirst();
  }
}
