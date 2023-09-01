package io.narsha.smartpage.core;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class RowMapper {

  private final ObjectMapper objectMapper;

  public RowMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper.copy();
    this.objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
  }

  public <R> R convert(Map<String, Object> map, Class<R> targetClass) {
    return this.objectMapper.convertValue(map, targetClass);
  }
}
