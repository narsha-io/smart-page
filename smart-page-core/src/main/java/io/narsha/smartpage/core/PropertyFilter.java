package io.narsha.smartpage.core;

/**
 * Wrapper for filter that need to be applied on the data source
 *
 * @param dataSourceProperty the data source column/property that will be filtered
 * @param value the filtered value
 * @param operation the filter operation
 */
public record PropertyFilter(String dataSourceProperty, Object value, String operation) {}
