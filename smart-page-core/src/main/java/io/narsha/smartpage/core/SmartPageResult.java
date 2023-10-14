package io.narsha.smartpage.core;

import java.util.List;

/**
 * Smart page result record
 *
 * @param data data resulted by the query
 * @param total how many total data for this query without pagination
 * @param <T> DTO type
 */
public record SmartPageResult<T>(List<T> data, Integer total) {}
