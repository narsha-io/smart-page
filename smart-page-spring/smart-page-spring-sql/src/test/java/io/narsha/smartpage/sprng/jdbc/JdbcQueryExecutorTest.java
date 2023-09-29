package io.narsha.smartpage.sprng.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.filters.ContainsFilter;
import io.narsha.smartpage.core.filters.EqualsFilter;
import io.narsha.smartpage.core.filters.InFilter;
import io.narsha.smartpage.spring.sql.JdbcQueryExecutor;
import io.narsha.smartpage.spring.sql.filters.JdbcFilterRegistrationService;
import io.narsha.smartpage.spring.test.SmartPageSpringTestApplication;
import io.narsha.smartpage.spring.test.model.Person;
import io.narsha.smartpage.spring.test.validator.PersonValidator;
import java.util.HashMap;
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

@SpringBootTest(classes = SmartPageSpringTestApplication.class)
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
                new PaginatedFilteredQuery<>(
                    Person.class, new HashMap<>(), new HashMap<>(), page, size));
    assertThat(res.result()).hasSize(exceptedPageSize);
    assertThat(res.totalResult()).isEqualTo(Long.valueOf(exceptedTotalElement));
    PersonValidator.validate(res.result());
  }

  private static Stream<Arguments> simplePaginatedTest() {
    return Stream.of(Arguments.of(0, 10, 5, 5), Arguments.of(0, 2, 2, 5), Arguments.of(2, 2, 1, 5));
  }

  @Test
  @Order(4)
  void paginationTestSortedPage0() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    query.orders().put("first_name", "asc");

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.result()).hasSize(2);
    assertThat(res.totalResult()).isEqualTo(5L);
    PersonValidator.containsIds(res.result(), Set.of(1L, 5L));
    PersonValidator.validate(res.result());
  }

  @Test
  @Order(5)
  void paginationTestSortedPage1() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 1, 2);
    query.orders().put("first_name", "asc");

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.result()).hasSize(2);
    assertThat(res.totalResult()).isEqualTo(5L);
    PersonValidator.containsIds(res.result(), Set.of(4L, 2L));
    PersonValidator.validate(res.result());
  }

  @Test
  @Order(6)
  void paginationTestEqualsStringFilter() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    var filter = new EqualsFilter<>(String.class);
    filter.parse(new ObjectMapper(), new String[] {"Perceval"});
    query.filters().put("first_name", filter);

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.result()).hasSize(1);
    assertThat(res.totalResult()).isEqualTo(1L);
    PersonValidator.containsIds(res.result(), Set.of(3L));
    PersonValidator.validate(res.result());
  }

  @Test
  @Order(7)
  void paginationTestEqualsLongFilter() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    var filter = new EqualsFilter<>(Long.class);
    filter.parse(new ObjectMapper(), new String[] {"2"});
    query.filters().put("id", filter);

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.result()).hasSize(1);
    assertThat(res.totalResult()).isEqualTo(1L);
    PersonValidator.containsIds(res.result(), Set.of(2L));
    PersonValidator.validate(res.result());
  }

  @Test
  @Order(8)
  void paginationTestInLongFilter() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    var filter = new InFilter<>(Long.class);
    filter.parse(new ObjectMapper(), new String[] {"2", "3"});
    query.filters().put("id", filter);

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.result()).hasSize(2);
    assertThat(res.totalResult()).isEqualTo(2L);
    PersonValidator.containsIds(res.result(), Set.of(2L, 3L));
    PersonValidator.validate(res.result());
  }

  @Test
  @Order(9)
  void paginationTestInStringFilter() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    var filter = new InFilter<>(String.class);
    filter.parse(new ObjectMapper(), new String[] {"Perceval", "Leodagan"});
    query.filters().put("first_name", filter);

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.result()).hasSize(2);
    assertThat(res.totalResult()).isEqualTo(2L);
    PersonValidator.containsIds(res.result(), Set.of(2L, 3L));
    PersonValidator.validate(res.result());
  }

  @Test
  @Order(10)
  void paginationTestContainsStringFilter() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    var filter = new ContainsFilter();
    filter.parse(new ObjectMapper(), new String[] {"Ka"});
    query.filters().put("first_name", filter);

    final var res =
        new JdbcQueryExecutor(jdbcTemplate, jdbcFilterRegistrationService, rowMapper)
            .execute(query);
    assertThat(res.result()).hasSize(2);
    assertThat(res.totalResult()).isEqualTo(2L);
    PersonValidator.containsIds(res.result(), Set.of(4L, 5L));
    PersonValidator.validate(res.result());
  }
}
