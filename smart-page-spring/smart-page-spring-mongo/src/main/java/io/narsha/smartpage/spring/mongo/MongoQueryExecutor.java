package io.narsha.smartpage.spring.mongo;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.utils.AnnotationUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class MongoQueryExecutor implements QueryExecutor {

  private final MongoTemplate mongoTemplate;

  @Override
  public <T> Pair<List<T>, Long> execute(
      PaginatedFilteredQuery<T> paginatedFilteredQuery, RowMapper rowMapper) {

    var pageable = PageRequest.of(paginatedFilteredQuery.page(), paginatedFilteredQuery.size());

    var orders =
        paginatedFilteredQuery.orders().entrySet().stream()
            .map(
                order -> {
                  return Sort.Order.by(order.getKey())
                      .with(Sort.Direction.fromString(order.getValue()));
                })
            .toList();

    final var pageRequest =
        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));

    Query query = new Query().with(pageRequest);
    // .skip(pageable.getPageSize() * pageable.getPageNumber()) // offset
    // .limit(pageable.getPageSize());

    // Add Filtered
    //    query.addCriteria(Criteria.where("projectId").is(projectId));

    final var collection =
        AnnotationUtils.getClassAnnotationValue(
            paginatedFilteredQuery.targetClass(), DataTable.class, DataTable::value);

    final var filtered = mongoTemplate.find(query, Map.class, collection);

    final var res =
        filtered.stream()
            .map(map -> (T) convert(paginatedFilteredQuery.targetClass(), map, rowMapper))
            .toList();
    // must transform object to target here
    long count = mongoTemplate.count(query.skip(-1).limit(-1), collection);

    return Pair.of(res, count);
  }

  private <T> T convert(Class<T> targetClass, Map<Object, Object> map, RowMapper rowMapper) {
    final var convertedMap = new HashMap<String, Object>();
    convertSubMap(targetClass, map, null, convertedMap);
    return rowMapper.convert(convertedMap, targetClass);
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
