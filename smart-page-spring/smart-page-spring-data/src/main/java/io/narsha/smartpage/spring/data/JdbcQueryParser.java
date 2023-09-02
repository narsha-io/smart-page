package io.narsha.smartpage.spring.data;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.QueryExecutor;
import io.narsha.smartpage.core.annotations.AnnotationUtils;
import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.annotations.DataTableProperty;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class JdbcQueryParser<T> {

  private StringBuilder query = new StringBuilder();
  private StringBuilder countQuery = new StringBuilder();
  private final PaginatedFilteredQuery<T> queryFilter;

  public void init() {
    buildQuery();
    buildCountQuery();
    buildOffsetQuery();
  }

  private StringBuilder offsetQuery = new StringBuilder();

  private void buildOffsetQuery() {
    if (this.queryFilter.getPage() != null
        && this.queryFilter.getPage() >= 0
        && this.queryFilter.getSize() != null
        && this.queryFilter.getSize() > 0) {
      var offsetStart = this.queryFilter.getPage() * this.queryFilter.getSize();
      this.offsetQuery
          .append(" LIMIT ")
          .append(this.queryFilter.getSize())
          .append(" OFFSET ")
          .append(offsetStart);
    }
  }

  public String getQuery() {
    return this.query.toString() + this.offsetQuery.toString();
  }

  public String getCountQuery() {
    return this.countQuery.toString();
  }

  private void buildQuery() {
    this.query.append(getBaseQuery());
    this.query.append(getFilterFragment(this.query.toString()));
    this.query.append(getOrderFragment());
  }

  private void buildCountQuery() {
    this.countQuery.append("select count(1) from (");
    this.countQuery.append(this.query);
    this.countQuery.append(") C");
  }

  private String getFilterFragment(String currentQuery) {
    final var filterBuilder = new StringBuilder();

    if (!isWhereClauseExists(currentQuery)) {
      filterBuilder.append(" where 1 = 1 ");
    }

    this.queryFilter
        .getFilters()
        .forEach(
            (prop, parser) -> {
              final var sqlField = getFilterParamValue(prop);
              filterBuilder.append(" and ").append(sqlField).append(parser.getSQLFragment(prop));
            });

    return filterBuilder.toString();
  }

  private boolean isWhereClauseExists(String query) {
    final var whereIndex = query.lastIndexOf("where");
    final var index = query.lastIndexOf(")");

    return index < whereIndex;
  }

  private String getBaseQuery() {
    final var reference =
        AnnotationUtils.getClassAnnotationValue(
            this.queryFilter.getTargetClass(), DataTable.class, DataTable::value);
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
    return "select * from ( " + res + " ) SMART_PAGE_QUERY";
  }

  private String getFilterParamValue(String prop) {
    try {
      return AnnotationUtils.getFieldAnnotationValue(
          this.queryFilter.getTargetClass(),
          prop,
          DataTableProperty.class,
          DataTableProperty::columnName);
    } catch (Exception e) {
      return prop;
    }
  }

  private String getOrderFragment() {
    var order =
        StreamSupport.stream(this.queryFilter.getOrders().entrySet().spliterator(), false)
            .map(
                o -> {
                  var prop = getFilterParamValue(o.getKey());
                  return prop + " " + o.getValue();
                })
            .collect(Collectors.joining(", "));

    if (StringUtils.isNotEmpty(order)) {
      order = " order by " + order;
    }
    return order;
  }
}
