package io.narsha.smartpage.spring.sql;

import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.core.SmartPageResult;
import io.narsha.smartpage.core.utils.ResolverUtils;
import io.narsha.smartpage.spring.sql.filters.JdbcFilterRegistrationService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/** In charge of the sql query execution */
@RequiredArgsConstructor
public class JdbcQueryExecutor implements QueryExecutor {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final JdbcFilterRegistrationService jdbcFilterRegistrationService;
  private final RowMapper rowMapper;

  @Override
  public <T> SmartPageResult<T> execute(SmartPageQuery<T> paginatedFilteredQuery) {

    final var jdbcQueryParser =
        new JdbcQueryParser<>(paginatedFilteredQuery, jdbcFilterRegistrationService);

    jdbcQueryParser.init();

    final var params =
        paginatedFilteredQuery.filters().entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    v ->
                        jdbcFilterRegistrationService
                            .get(v.getValue().getClass())
                            .map(t -> t.getParsedValue(v.getValue().getValue()))
                            .orElse(v.getValue().getValue())));

    final var data =
        this.jdbcTemplate.query(
            jdbcQueryParser.getQuery(),
            params,
            rs -> {
              return extractResultSet(paginatedFilteredQuery, rowMapper, rs);
            });

    final var count =
        this.jdbcTemplate.query(
            jdbcQueryParser.getCountQuery(),
            params,
            rs -> {
              rs.next();
              return rs.getInt(1);
            });

    return new SmartPageResult<>(data, count);
  }

  private <T> List<T> extractResultSet(
      SmartPageQuery<T> paginatedFilteredQuery, RowMapper rowMapper, ResultSet rs)
      throws SQLException {
    final var queryDefinition = getQueryDefinition(rs);

    final var result = new ArrayList<T>();
    while (rs.next()) {
      final var object = new HashMap<String, Object>();

      for (var entry : queryDefinition.entrySet()) {
        var javaProperty =
            ResolverUtils.getJavaProperty(paginatedFilteredQuery.targetClass(), entry.getKey());
        if (javaProperty.isPresent()) {
          object.put(javaProperty.get(), rs.getObject(entry.getValue()));
        }
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
