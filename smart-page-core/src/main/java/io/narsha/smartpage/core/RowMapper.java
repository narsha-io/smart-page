package io.narsha.smartpage.core;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/** Class used to map a data from the datasource query into a java DTO */
public class RowMapper {

  /** Jackson objectMapper used to convert property */
  private final ObjectMapper objectMapper;

  /**
   * Constructor which make a copy of the objectMapper
   *
   * @param objectMapper Jackson ObjectMapper
   */
  public RowMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper.copy();
    this.objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
  }

  /**
   * Convert a query data as a Map into a DTO
   *
   * @param map query data as Map
   * @param targetClass DTO class which represent the targeted Map
   * @return an instance of DTO filled with query data
   * @param <R> DTO type
   */
  public <R> R convert(Map<String, Object> map, Class<R> targetClass) {
    return this.objectMapper.convertValue(map, targetClass);
  }
}
