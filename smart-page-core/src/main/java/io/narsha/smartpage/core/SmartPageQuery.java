package io.narsha.smartpage.core;

import java.util.Map;

/**
 * An object that contains everything needed for executing datasource query
 *
 * @param targetClass The DTO class
 * @param filters all property filters
 * @param orders order need to be set during the datasource query
 * @param page used for pagination
 * @param size used for pagination
 * @param <T> The DTO type
 */
public record SmartPageQuery<T>(
    Class<T> targetClass,
    Map<String, Object> filters,
    Map<String, String> orders,
    Integer page,
    Integer size) {}
