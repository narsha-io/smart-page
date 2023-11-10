package io.narsha.smartpage.core.sql;

import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.core.exceptions.InternalException;
import io.narsha.smartpage.core.sql.filters.JdbcFilterRegistrationService;
import io.narsha.smartpage.core.utils.ResolverUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Use to generate a sql query from a PaginatedFilteredQuery
 *
 * @param <T> type of the target DTO
 */
@RequiredArgsConstructor
public class JdbcQueryParser<T> {

  private StringBuilder query = new StringBuilder();
  private StringBuilder countQuery = new StringBuilder();
  private final SmartPageQuery<T> queryFilter;
  private final JdbcFilterRegistrationService jdbcFilterRegistrationService;

  /** Build sql + count query */
  public void init() {
    buildQuery();
    buildCountQuery();
    buildOffsetQuery();
  }

  private StringBuilder offsetQuery = new StringBuilder();

  private void buildOffsetQuery() {
    if (this.queryFilter.page() != null
        && this.queryFilter.page() >= 0
        && this.queryFilter.size() != null
        && this.queryFilter.size() > 0) {
      var offsetStart = this.queryFilter.page() * this.queryFilter.size();
      this.offsetQuery
          .append(" LIMIT ")
          .append(this.queryFilter.size())
          .append(" OFFSET ")
          .append(offsetStart);
    }
  }

  /**
   * Get the full sql query
   *
   * @return the sql data query
   */
  public String getQuery() {
    return this.query.toString() + this.offsetQuery.toString();
  }

  /**
   * Get the count query
   *
   * @return the count query
   */
  public String getCountQuery() {
    return this.countQuery.toString();
  }

  private void buildQuery() {
    this.query.append(getBaseQuery());
    this.query.append(getFilterFragment());
    this.query.append(getOrderFragment());
  }

  private void buildCountQuery() {
    this.countQuery.append("select count(1) from (");
    this.countQuery.append(this.query);
    this.countQuery.append(") C");
  }

  private String getFilterFragment() {
    final var filterBuilder = new StringBuilder();

    this.queryFilter
        .filters()
        .forEach(
            propertyFilter -> {
              var filter =
                  jdbcFilterRegistrationService
                      .get(propertyFilter.operation())
                      .orElseThrow(InternalException::new);
              filterBuilder
                  .append(" AND ")
                  .append(filter.getSQLFragment(propertyFilter.dataSourceProperty()));
            });

    return filterBuilder.toString();
  }

  private String getBaseQuery() {
    final var reference =
        ResolverUtils.getDataTableValue(
            this.queryFilter.targetClass(), SqlDataTable.class, SqlDataTable::query);
    return getSQLFileContent(reference);
  }

  private String getSQLFileContent(String reference) {
    String res;
    try (final var inputStream = QueryExecutor.class.getResourceAsStream(reference);
        final var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        final var bufferedReader = new BufferedReader(inputStreamReader)) {
      res = bufferedReader.lines().collect(Collectors.joining(" "));
    } catch (Exception e) {
      res = reference;
    }
    return "SELECT * FROM ( " + res + " ) SMART_PAGE_QUERY WHERE 1 = 1 ";
  }

  private String getOrderFragment() {
    var order =
        this.queryFilter.orders().entrySet().stream()
            .map(o -> o.getKey() + " " + o.getValue())
            .collect(Collectors.joining(", "));

    if (StringUtils.isNotEmpty(order)) {
      order = " ORDER BY " + order;
    }
    return order;
  }
}
