package io.narsha.smartpage.core;

import io.narsha.smartpage.core.filters.FilterParser;

import java.util.HashMap;
import java.util.Map;

public class PaginatedFilteredQuery<T> {

    private final Class<T> targetClass;
    private final Map<String, FilterParser> filters = new HashMap<>();
    private final Map<String, String> orders = new HashMap<>();
    private final Integer page;
    private final Integer size;

    public PaginatedFilteredQuery(Class<T> targetClass, Integer page, Integer size) {
        this.targetClass = targetClass;
        this.page = page;
        this.size = size;
    }


    public Class<T> getTargetClass() {
        return this.targetClass;
    }

    public Map<String, FilterParser> getFilters() {
        return filters;
    }

    public Map<String, String> getOrders() {
        return orders;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }
}
