package io.narsha.smartpage.spring.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import io.narsha.smartpage.core.PropertyFilter;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.core.annotations.DataTableProperty;
import io.narsha.smartpage.spring.mongo.filters.MongoFilterRegistrationService;
import io.narsha.smartpage.spring.test.SmartPageSpringTestApplication;
import io.narsha.smartpage.spring.test.model.Person;
import io.narsha.smartpage.spring.test.validator.PersonValidator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = {SmartPageSpringTestApplication.class})
@ContextConfiguration(initializers = MongoQueryExecutorTest.Initializer.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MongoQueryExecutorTest {

  @Autowired private MongoTemplate mongoTemplate;
  @Autowired private MongoFilterRegistrationService mongoFilterRegistrationService;
  @Autowired private RowMapper rowMapper;

  private static final String COLLECTION = "person";

  @BeforeEach
  void ini() {
    mongoTemplate.getDb().getCollection(COLLECTION).drop();
    mongoTemplate.getDb().createCollection(COLLECTION);
    mongoTemplate
        .getDb()
        .getCollection(COLLECTION)
        .insertMany(
            List.of(
                new Document(
                    Map.of(
                        "_id",
                        1L,
                        "first_name",
                        "Arthur",
                        "parent_role",
                        Map.of("role", "Roi du royaume de Logres"))),
                new Document(
                    Map.of(
                        "_id",
                        2L,
                        "first_name",
                        "Leodagan",
                        "parent_role",
                        Map.of("role", "Roi de Carmélide"))),
                new Document(
                    Map.of(
                        "_id",
                        3L,
                        "first_name",
                        "Perceval",
                        "parent_role",
                        Map.of("role", "Venu des etoiles"))),
                new Document(
                    Map.of(
                        "_id",
                        4L,
                        "first_name",
                        "Karadoc",
                        "parent_role",
                        Map.of("role", "Le croque monsieur"))),
                new Document(
                    Map.of(
                        "_id",
                        5L,
                        "first_name",
                        "Kadoc",
                        "parent_role",
                        Map.of("role", "Elle est où la poulette ?")))));
  }

  @Order(1)
  @ParameterizedTest
  @MethodSource("simplePaginatedTest")
  void mappingTest(
      Integer page, Integer size, Integer exceptedPageSize, Integer exceptedTotalElement) {
    final var res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService, rowMapper)
            .execute(
                new SmartPageQuery<>(
                    MongoPerson.class, new HashSet<>(), new HashMap<>(), page, size),
                null);
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
        new SmartPageQuery<>(MongoPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);
    query.orders().put("first_name", "asc");

    final var res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService, rowMapper)
            .execute(query, null);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(5);
    PersonValidator.containsIds(res.data(), Set.of(1L, 5L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(5)
  void paginationTestSortedPage1() {
    final var query =
        new SmartPageQuery<>(MongoPerson.class, new HashSet<>(), new HashMap<>(), 1, 2);
    query.orders().put("first_name", "asc");

    final var res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService, rowMapper)
            .execute(query, null);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(5);
    PersonValidator.containsIds(res.data(), Set.of(4L, 2L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(6)
  void paginationTestEqualsStringFilter() {
    final var query =
        new SmartPageQuery<>(MongoPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);

    query.filters().add(new PropertyFilter("first_name", "Perceval", "equals"));

    final var res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService, rowMapper)
            .execute(query, null);
    assertThat(res.data()).hasSize(1);
    assertThat(res.total()).isEqualTo(1);
    PersonValidator.containsIds(res.data(), Set.of(3L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(7)
  void paginationTestEqualsLongFilter() {
    final var query =
        new SmartPageQuery<>(MongoPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);
    query.filters().add(new PropertyFilter("_id", 2L, "equals"));

    final var res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService, rowMapper)
            .execute(query, null);
    assertThat(res.data()).hasSize(1);
    assertThat(res.total()).isEqualTo(1);
    PersonValidator.containsIds(res.data(), Set.of(2L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(8)
  void paginationTestInLongFilter() {
    final var query =
        new SmartPageQuery<>(MongoPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);
    query.filters().add(new PropertyFilter("_id", Set.of(2L, 3L), "in"));

    final var res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService, rowMapper)
            .execute(query, null);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(2);
    PersonValidator.containsIds(res.data(), Set.of(2L, 3L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(9)
  void paginationTestInStringFilter() {
    final var query =
        new SmartPageQuery<>(MongoPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);
    query.filters().add(new PropertyFilter("first_name", Set.of("Perceval", "Leodagan"), "in"));

    final var res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService, rowMapper)
            .execute(query, null);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(2);
    PersonValidator.containsIds(res.data(), Set.of(2L, 3L));
    PersonValidator.validate(res.data());
  }

  @Test
  @Order(10)
  void paginationTestContainsStringFilter() {
    final var query =
        new SmartPageQuery<>(MongoPerson.class, new HashSet<>(), new HashMap<>(), 0, 2);
    query.filters().add(new PropertyFilter("first_name", "Ka", "contains"));

    final var res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService, rowMapper)
            .execute(query, null);
    assertThat(res.data()).hasSize(2);
    assertThat(res.total()).isEqualTo(2);
    PersonValidator.containsIds(res.data(), Set.of(4L, 5L));
    PersonValidator.validate(res.data());
  }

  @MongoDataTable(collection = COLLECTION)
  private static class MongoPerson extends Person {

    @DataTableProperty(columnName = "_id")
    private Long id;

    @DataTableProperty(columnName = "first_name")
    private String firstName;

    @DataTableProperty(columnName = "parent_role.role")
    private String role;
  }

  public static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static MongoDBContainer mongoDBContainer;

    public Initializer() {
      if (mongoDBContainer == null) {
        mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7.0.0"));
        mongoDBContainer.start();
      }
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues values =
          TestPropertyValues.of(
              "spring.data.mongodb.host=" + mongoDBContainer.getHost(),
              "spring.data.mongodb.port=" + mongoDBContainer.getFirstMappedPort());
      values.applyTo(configurableApplicationContext);
    }
  }
}
