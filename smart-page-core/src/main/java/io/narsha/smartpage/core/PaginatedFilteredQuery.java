package io.narsha.smartpage.core;

import io.narsha.smartpage.core.filters.FilterParser;
import java.util.Map;

public record PaginatedFilteredQuery<T>(
    Class<T> targetClass,
    Map<String, FilterParser<?, ?>> filters,
    Map<String, String> orders,
    Integer page,
    Integer size) {}
