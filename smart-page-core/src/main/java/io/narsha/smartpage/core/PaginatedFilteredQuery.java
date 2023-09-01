package io.narsha.smartpage.core;

import io.narsha.smartpage.core.filters.FilterParser;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaginatedFilteredQuery<T> {

  private final Class<T> targetClass;
  private Map<String, FilterParser> filters = new HashMap<>();
  private Map<String, String> orders = new HashMap<>();
  private final Integer page;
  private final Integer size;
}
