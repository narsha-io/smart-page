package io.narsha.smartpage.spring.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.annotations.DataTableProperty;
import io.narsha.smartpage.core.filters.ContainsFilter;
import io.narsha.smartpage.core.filters.EqualsFilter;
import io.narsha.smartpage.core.filters.InFilter;
import io.narsha.smartpage.spring.mongo.filters.MongoFilterRegistrationService;
import io.narsha.smartpage.spring.test.SmartPageSpringTestApplication;
import io.narsha.smartpage.spring.test.validator.PersonValidator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

  private final ObjectMapper objectMapper = new ObjectMapper();
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

  @Test
  @Order(1)
  void mappingTest() {
    final Pair<List<Person>, Long> res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService)
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
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService)
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
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService)
            .execute(
                new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 2, 2),
                new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(1);
    assertThat(res.getValue()).isEqualTo(5L);
    PersonValidator.validate(res.getKey());
  }

  @Test
  @Order(4)
  void paginationTestSortedPage0() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    query.orders().put("first_name", "asc");

    final Pair<List<Person>, Long> res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService)
            .execute(query, new RowMapper(objectMapper));
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
    query.orders().put("first_name", "asc");

    final Pair<List<Person>, Long> res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService)
            .execute(query, new RowMapper(objectMapper));
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
    var filter = new EqualsFilter<>(String.class);
    filter.parse(new ObjectMapper(), new String[] {"Perceval"});
    query.filters().put("first_name", filter);

    final Pair<List<Person>, Long> res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService)
            .execute(query, new RowMapper(objectMapper));
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
    var filter = new EqualsFilter<>(Long.class);
    filter.parse(new ObjectMapper(), new String[] {"2"});
    query.filters().put("_id", filter);

    final Pair<List<Person>, Long> res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService)
            .execute(query, new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(1);
    assertThat(res.getValue()).isEqualTo(1L);
    PersonValidator.containsIds(res.getKey(), Set.of(2L));
    PersonValidator.validate(res.getKey());
  }

  @Test
  @Order(8)
  void paginationTestInLongFilter() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    var filter = new InFilter<>(Long.class);
    filter.parse(new ObjectMapper(), new String[] {"2", "3"});
    query.filters().put("_id", filter);

    final Pair<List<Person>, Long> res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService)
            .execute(query, new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(2);
    assertThat(res.getValue()).isEqualTo(2L);
    PersonValidator.containsIds(res.getKey(), Set.of(2L, 3L));
    PersonValidator.validate(res.getKey());
  }

  @Test
  @Order(9)
  void paginationTestInStringFilter() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    var filter = new InFilter<>(String.class);
    filter.parse(new ObjectMapper(), new String[] {"Perceval", "Leodagan"});
    query.filters().put("first_name", filter);

    final Pair<List<Person>, Long> res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService)
            .execute(query, new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(2);
    assertThat(res.getValue()).isEqualTo(2L);
    PersonValidator.containsIds(res.getKey(), Set.of(2L, 3L));
    PersonValidator.validate(res.getKey());
  }

  @Test
  @Order(10)
  void paginationTestContainsStringFilter() {
    final var query =
        new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 2);
    var filter = new ContainsFilter();
    filter.parse(new ObjectMapper(), new String[] {"Ka"});
    query.filters().put("first_name", filter);

    final Pair<List<Person>, Long> res =
        new MongoQueryExecutor(mongoTemplate, mongoFilterRegistrationService)
            .execute(query, new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(2);
    assertThat(res.getValue()).isEqualTo(2L);
    PersonValidator.containsIds(res.getKey(), Set.of(4L, 5L));
    PersonValidator.validate(res.getKey());
  }

  @DataTable(value = COLLECTION)
  private static class Person extends io.narsha.smartpage.spring.test.model.Person {

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
