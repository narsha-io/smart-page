package io.narsha.smartpage.spring.jdbc;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcQueryExecutor implements QueryExecutor {

  private final JdbcTemplate jdbcTemplate;

  public JdbcQueryExecutor(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public <T> Pair<List<T>, Long> execute(
      PaginatedFilteredQuery<T> paginatedFilteredQuery, RowMapper rowMapper) {
    /*
           List<T> data = this.jdbcTemplate.query(paginatedFilteredQuery.getQuery(), rs -> {
               return extractResultSet(paginatedFilteredQuery, rowMapper, rs);
           });

           Long count = 1L;

           return Pair.of(data, count);

    */
    return null;
  }

  private <T> List<T> extractResultSet(
      PaginatedFilteredQuery<T> paginatedFilteredQuery, RowMapper rowMapper, ResultSet rs)
      throws SQLException {
    final var queryDefinition = getQueryDefinition(rs);

    final var result = new ArrayList<T>();
    while (rs.next()) {
      final var object = new HashMap<String, Object>();

      for (var entry : queryDefinition.entrySet()) {
        object.put(entry.getKey(), rs.getObject(entry.getValue()));
      }
      result.add(rowMapper.convert(object, paginatedFilteredQuery.getTargetClass()));
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
