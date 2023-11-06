package io.narsha.smartpage.spring.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import io.narsha.smartpage.core.PropertyFilter;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.spring.sql.JdbcQueryExecutor;
import io.narsha.smartpage.spring.sql.SqlDataTable;
import io.narsha.smartpage.spring.sql.filters.JdbcFilterRegistrationService;
import io.narsha.smartpage.spring.test.SmartPageSpringTestApplication;
import io.narsha.smartpage.spring.test.model.Person;
import io.narsha.smartpage.spring.test.validator.PersonValidator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SpringBootTest(classes = {SmartPageSpringTestApplication.class, JdbcConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcQueryExecutorTest {

  @Autowired private NamedParameterJdbcTemplate jdbcTemplate;

  @Autowired private JdbcFilterRegistrationService jdbcFilterRegistrationService;
  @Autowired private RowMapper rowMapper;

  @Order(1)
  @ParameterizedTest
  @MethodSource("simplePaginatedTest")
  void mappingTest(
      Integer page, Integer size, Integer exceptedPageSize, Integer exceptedTotalElement) {
    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(
                new SmartPageQuery<>(
                    JdbcPerson.class, new HashSet<>(), new HashMap<>(), page, size));
    assertThat(res.data()).hasSize(exceptedPageSize);
    assertThat(res.total()).isEqualTo(exceptedTotalElement);
    PersonValidator.validate(res.data());
  }

  private static Stream<Arguments> simplePaginatedTest() {
    return Stream.of(Arguments.of(0, 10, 5, 5), Arguments.of(0, 2, 2, 5), Arguments.of(2, 2, 1, 5));
  }

  @Test
  @Order(4)
  void paginationTestSortedPage0() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);
    query.orders().put("first_name", "asc");

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(5);
    PersonValidator.containsIds(res.data(), Set.of(1L, 5L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(5)
  void paginationTestSortedPage1() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 1, 2);
    query.orders().put("first_name", "asc");

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(5);
    PersonValidator.containsIds(res.data(), Set.of(4L, 2L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(6)
  void paginationTestEqualsStringFilter() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);
    query.filters().add(new PropertyFilter("first_name", "Perceval", "equals"));

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(1);
    assertThat(res.total()).isEqualTo(1);
    PersonValidator.containsIds(res.data(), Set.of(3L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(7)
  void paginationTestEqualsLongFilter() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);

    query.filters().add(new PropertyFilter("id", "2", "equals"));

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(1);
    assertThat(res.total()).isEqualTo(1);
    PersonValidator.containsIds(res.data(), Set.of(2L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(8)
  void paginationTestInLongFilter() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);

    query.filters().add(new PropertyFilter("id", Set.of(2, 3), "in"));

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(2);
    PersonValidator.containsIds(res.data(), Set.of(2L, 3L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(9)
  void paginationTestInStringFilter() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);

    query.filters().add(new PropertyFilter("first_name", Set.of("Perceval", "Leodagan"), "in"));

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(2);
    PersonValidator.containsIds(res.data(), Set.of(2L, 3L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(10)
  void paginationTestContainsStringFilter() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);

    query.filters().add(new PropertyFilter("first_name", "%Ka%", "contains"));

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(2);
    PersonValidator.containsIds(res.data(), Set.of(4L, 5L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(11)
  void paginationTestGreaterThanFilter() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 0, 10);

    query.filters().add(new PropertyFilter("first_name", Set.of("Karadoc"), "gt"));

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(2);
    PersonValidator.containsIds(res.data(), Set.of(2L, 3L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(12)
  void paginationTestGreaterThanOrEqualsFilter() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 0, 10);

    query.filters().add(new PropertyFilter("first_name", Set.of("Karadoc"), "gte"));

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(3);
    assertThat(res.total()).isEqualTo(3);
    PersonValidator.containsIds(res.data(), Set.of(2L, 3L, 4L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(13)
  void paginationTestLessThanFilter() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 0, 10);

    query.filters().add(new PropertyFilter("first_name", Set.of("Karadoc"), "lt"));

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(2);
    PersonValidator.containsIds(res.data(), Set.of(1L, 5L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(14)
  void paginationTestLessThanOrEqualsFilter() {
    final var query =
        new SmartPageQuery<>(JdbcPerson.class, new HashSet<>(), new HashMap<>(), 0, 10);

    query.filters().add(new PropertyFilter("first_name", Set.of("Karadoc"), "lte"));

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.data()).hasSize(3);
    assertThat(res.total()).isEqualTo(3);
    PersonValidator.containsIds(res.data(), Set.of(1L, 4L, 5L));
    PersonValidator.validate(res.data());
  }

  @SqlDataTable(query = "select id, first_name, role from person")
  public static class JdbcPerson extends Person {}
}
