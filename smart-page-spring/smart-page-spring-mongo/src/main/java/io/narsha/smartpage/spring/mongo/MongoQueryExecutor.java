package io.narsha.smartpage.spring.mongo;

import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.core.SmartPageResult;
import io.narsha.smartpage.core.exceptions.InternalException;
import io.narsha.smartpage.core.utils.ResolverUtils;
import io.narsha.smartpage.spring.mongo.filters.MongoFilterRegistrationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/** Class that will execute the mongo query following the declared filters */
@Slf4j
public class MongoQueryExecutor implements QueryExecutor<List<Criteria>> {

  private final MongoTemplate mongoTemplate;

  private final MongoFilterRegistrationService mongoFilterRegistrationService;
  private final RowMapper rowMapper;

  /**
   * constructor
   *
   * @param mongoTemplate mongo template in charge of the communication with mongo instance
   * @param mongoFilterRegistrationService auto registered mongo filters service
   * @param rowMapper conversion mapper
   */
  public MongoQueryExecutor(
      MongoTemplate mongoTemplate,
      MongoFilterRegistrationService mongoFilterRegistrationService,
      RowMapper rowMapper) {
    this.mongoTemplate = mongoTemplate;
    this.mongoFilterRegistrationService = mongoFilterRegistrationService;
    this.rowMapper = rowMapper;
  }

  @Override
  public <T> SmartPageResult<T> execute(
      SmartPageQuery<T> paginatedFilteredQuery, List<Criteria> extraParameters) {

    var pageable = PageRequest.of(paginatedFilteredQuery.page(), paginatedFilteredQuery.size());

    // TODO move in an utils class
    var orders =
        paginatedFilteredQuery.orders().entrySet().stream()
            .map(
                order ->
                    Sort.Order.by(order.getKey()).with(Sort.Direction.fromString(order.getValue())))
            .toList();

    final var pageRequest =
        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));

    Query query = new Query().with(pageRequest);

    paginatedFilteredQuery.filters().stream()
        .forEach(
            v -> {
              var filter =
                  this.mongoFilterRegistrationService
                      .get(v.operation())
                      .orElseThrow(InternalException::new);

              var criteria = filter.getMongoCriteria(v.dataSourceProperty(), v.value());
              query.addCriteria(criteria);
            });

    if (extraParameters != null) {
      extraParameters.forEach(query::addCriteria);
    }

    final var collection =
        ResolverUtils.getDataTableValue(
            paginatedFilteredQuery.targetClass(), MongoDataTable.class, MongoDataTable::collection);

    final var filtered = mongoTemplate.find(query, Map.class, collection);

    final var res =
        filtered.stream()
            .map(map -> (T) convert(paginatedFilteredQuery.targetClass(), map, rowMapper))
            .toList();
    int count = Math.toIntExact(mongoTemplate.count(query.skip(-1).limit(-1), collection));

    return new SmartPageResult<>(res, count);
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
            ResolverUtils.getJavaProperty(targetClass, key)
                .ifPresent(javaProperty -> outputMap.put(javaProperty, v));
          }
        });
  }
}
