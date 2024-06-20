package io.narsha.smartpage.core;

import java.util.Map;
import java.util.Set;

/**
 * An object that contains everything needed for executing datasource query
 *
 * @param <T> kind of DTO
 * @param targetClass The DTO class
 * @param filters all property filters
 * @param orders order need to be set during the datasource query
 * @param page used for pagination
 * @param size used for pagination
 */
public record SmartPageQuery<T>(
    Class<T> targetClass,
    Set<PropertyFilter> filters,
    Map<String, String> orders,
    Integer page,
    Integer size) {}
