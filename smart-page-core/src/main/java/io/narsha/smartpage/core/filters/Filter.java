package io.narsha.smartpage.core.filters;

import java.util.function.Supplier;

public enum Filter {
  EQUALS(EqualsFilter::new);

  final Supplier<FilterParser> parser;

  Filter(Supplier<FilterParser> parser) {
    this.parser = parser;
  }

  public Supplier<FilterParser> getParser() {
    return this.parser;
  }
}
