package io.narsha.smartpage.spring.data;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.annotations.AnnotationUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcQueryExecutor implements QueryExecutor {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public JdbcQueryExecutor(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public <T> Pair<List<T>, Long> execute(
      PaginatedFilteredQuery<T> paginatedFilteredQuery, RowMapper rowMapper) {

    final var jdbcQueryParser = new JdbcQueryParser<T>(paginatedFilteredQuery);
    jdbcQueryParser.init();

    var params =
        paginatedFilteredQuery.filters().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, f -> f.getValue().getValue()));

    List<T> data =
        this.jdbcTemplate.query(
            jdbcQueryParser.getQuery(),
            params,
            rs -> {
              return extractResultSet(paginatedFilteredQuery, rowMapper, rs);
            });

    Long count =
        this.jdbcTemplate.query(
            jdbcQueryParser.getCountQuery(),
            params,
            rs -> {
              rs.next();
              return rs.getLong(1);
            });

    return Pair.of(data, count);
  }

  private <T> List<T> extractResultSet(
      PaginatedFilteredQuery<T> paginatedFilteredQuery, RowMapper rowMapper, ResultSet rs)
      throws SQLException {
    final var queryDefinition = getQueryDefinition(rs);

    final var result = new ArrayList<T>();
    while (rs.next()) {
      final var object = new HashMap<String, Object>();

      for (var entry : queryDefinition.entrySet()) {
        object.put(
            AnnotationUtils.getJavaProperty(paginatedFilteredQuery.targetClass(), entry.getKey()),
            rs.getObject(entry.getValue()));
      }
      result.add(rowMapper.convert(object, paginatedFilteredQuery.targetClass()));
    }
    return result;
  }

  private Map<String, Integer> getQueryDefinition(ResultSet resultSet) throws SQLException {
    final var resultSetMetaData = resultSet.getMetaData();
    final var columnCount = resultSetMetaData.getColumnCount();

    final var columns = new HashMap<String, Integer>();
    for (int i = 1; i <= columnCount; i++) {
      final var label = resultSetMetaData.getColumnLabel(i);
      columns.put(label, i);
    }

    return columns;
  }
}
