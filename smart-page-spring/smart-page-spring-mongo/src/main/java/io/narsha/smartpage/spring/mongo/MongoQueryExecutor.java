package io.narsha.smartpage.spring.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.utils.AnnotationUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class MongoQueryExecutor implements QueryExecutor {

  private final MongoTemplate mongoTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public <T> Pair<List<T>, Long> execute(
      PaginatedFilteredQuery<T> paginatedFilteredQuery, RowMapper rowMapper) {

    var pageable = PageRequest.of(paginatedFilteredQuery.page(), paginatedFilteredQuery.size());

    Query query =
        new Query()
            .with(pageable)
            .skip(pageable.getPageSize() * pageable.getPageNumber()) // offset
            .limit(pageable.getPageSize());

    // Add Filtered
    //    query.addCriteria(Criteria.where("projectId").is(projectId));

    final var filtered = mongoTemplate.find(query, Map.class, "person");

    final var res =
        filtered.stream()
            .map(map -> (T) convert(paginatedFilteredQuery.targetClass(), map, objectMapper))
            .toList();
    // must transform object to target here
    long count = mongoTemplate.count(query.skip(-1).limit(-1), "person");

    return Pair.of(res, count);
  }

  private <T> T convert(Class<T> targetClass, Map<Object, Object> map, ObjectMapper objectMapper) {
    final var convertedMap = new HashMap<String, Object>();
    convertSubMap(targetClass, map, null, convertedMap);
    return objectMapper.convertValue(convertedMap, targetClass);
  }

  private <T> void convertSubMap(
      Class<T> targetClass,
      Map<Object, Object> sourceMap,
      String prefix,
      Map<String, Object> outputMap) {
    sourceMap.forEach(
        (k, v) -> {
          var key = (prefix != null ? (prefix + ".") : "") + k;
          if (v instanceof Map map) {
            convertSubMap(targetClass, map, key, outputMap);
          } else {
            AnnotationUtils.getJavaProperty(targetClass, key)
                .ifPresent(javaProperty -> outputMap.put(javaProperty, v));
          }
        });
  }
}
