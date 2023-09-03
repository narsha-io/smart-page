package io.narsha.smartpage.spring.mongo;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.RowMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class MongoQueryExecutor implements QueryExecutor {

  private final MongoTemplate mongoTemplate;

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

    List<T> filtered = mongoTemplate.find(query, paginatedFilteredQuery.targetClass(), "metadata");

    Page<T> page =
        PageableExecutionUtils.getPage(
            filtered,
            pageable,
            () ->
                mongoTemplate.count(
                    query.skip(-1).limit(-1), paginatedFilteredQuery.targetClass()));

    return Pair.of(filtered, page.getTotalElements());
  }
}
