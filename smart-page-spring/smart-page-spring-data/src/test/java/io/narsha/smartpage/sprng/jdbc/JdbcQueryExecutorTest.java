package io.narsha.smartpage.sprng.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.filters.EqualsFilter;
import io.narsha.smartpage.spring.data.JdbcQueryExecutor;
import io.narsha.smartpage.spring.test.SmartPageSpringTestApplication;
import io.narsha.smartpage.spring.test.model.Person;
import io.narsha.smartpage.spring.test.validator.PersonValidator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SpringBootTest(classes = {SmartPageSpringTestApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcQueryExecutorTest {

  @Autowired private NamedParameterJdbcTemplate jdbcTemplate;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @Order(1)
  void mappingTest() {
    final Pair<List<Person>, Long> res =
        new JdbcQueryExecutor(jdbcTemplate)
            .execute(
                new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 10),
                new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(5);
    assertThat(res.getValue()).isEqualTo(5L);
    PersonValidator.validate(res.getKey());
  }

  @Test
  @Order(2)
  void paginationTestPage0() {
    final Pair<List<Person>, Long> res =
        new JdbcQueryExecutor(jdbcTemplate)
            .execute(
                new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2),
                new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(2);
    assertThat(res.getValue()).isEqualTo(5L);
    PersonValidator.validate(res.getKey());
  }

  @Test
  @Order(3)
  void paginationTestPage1() {
    final Pair<List<Person>, Long> res =
        new JdbcQueryExecutor(jdbcTemplate)
            .execute(
                new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 1, 2),
                new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(2);
    assertThat(res.getValue()).isEqualTo(5L);
    PersonValidator.validate(res.getKey());
  }

  @Test
  @Order(4)
  void paginationTestSortedPage0() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    query.orders().put("firstName", "asc");

    final Pair<List<Person>, Long> res =
        new JdbcQueryExecutor(jdbcTemplate).execute(query, new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(2);
    assertThat(res.getValue()).isEqualTo(5L);
    PersonValidator.containsIds(res.getKey(), Set.of(1L, 5L));
    PersonValidator.validate(res.getKey());
  }

  @Test
  @Order(5)
  void paginationTestSortedPage1() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 1, 2);
    query.orders().put("firstName", "asc");

    final Pair<List<Person>, Long> res =
        new JdbcQueryExecutor(jdbcTemplate).execute(query, new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(2);
    assertThat(res.getValue()).isEqualTo(5L);
    PersonValidator.containsIds(res.getKey(), Set.of(4L, 2L));
    PersonValidator.validate(res.getKey());
  }

  @Test
  @Order(6)
  void paginationTestEqualsStringFilter() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    var filter = new EqualsFilter();
    filter.parse(new ObjectMapper(), String.class, new String[] {"Perceval"});
    query.filters().put("firstName", filter);

    final Pair<List<Person>, Long> res =
        new JdbcQueryExecutor(jdbcTemplate).execute(query, new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(1);
    assertThat(res.getValue()).isEqualTo(1L);
    PersonValidator.containsIds(res.getKey(), Set.of(3L));
    PersonValidator.validate(res.getKey());
  }

  @Test
  @Order(7)
  void paginationTestEqualsLongFilter() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    var filter = new EqualsFilter();
    filter.parse(new ObjectMapper(), String.class, new String[] {"2"});
    query.filters().put("id", filter);

    final Pair<List<Person>, Long> res =
        new JdbcQueryExecutor(jdbcTemplate).execute(query, new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(1);
    assertThat(res.getValue()).isEqualTo(1L);
    PersonValidator.containsIds(res.getKey(), Set.of(2L));
    PersonValidator.validate(res.getKey());
  }
}
