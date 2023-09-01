package io.narsha.smartpage.spring.jpa;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.RowMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class JpaQueryExecutor implements QueryExecutor {

  private final EntityManager entityManager;

  public JpaQueryExecutor(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public <T> Pair<List<T>, Long> execute(
      PaginatedFilteredQuery<T> paginatedFilteredQuery, RowMapper rowMapper) {
    final NativeQuery query =
        this.entityManager
            .createNativeQuery("select id, name from countries")
            .unwrap(NativeQuery.class);
    query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

    var res = extractData(query, paginatedFilteredQuery, rowMapper);

    return Pair.of(res, 1L);
  }

  private <T> List<T> extractData(
      NativeQuery query, PaginatedFilteredQuery<T> paginatedFilteredQuery, RowMapper rowMapper) {
    final List<T> res = new ArrayList<>();
    final List<Map<String, Object>> data = query.getResultList();

    for (var line : data) {
      res.add(rowMapper.convert(line, paginatedFilteredQuery.getTargetClass()));
    }

    return res;
  }
}
