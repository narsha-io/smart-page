package io.narsha.smartpage.spring.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.annotations.DataTableProperty;
import io.narsha.smartpage.spring.test.SmartPageSpringTestApplication;
import io.narsha.smartpage.spring.test.model.Person;
import io.narsha.smartpage.spring.test.validator.PersonValidator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void ini() {
    mongoTemplate.getDb().getCollection("person").drop();
    mongoTemplate.getDb().createCollection("person");
    mongoTemplate
        .getDb()
        .getCollection("person")
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
        new MongoQueryExecutor(mongoTemplate, new ObjectMapper())
            .execute(
                new PaginatedFilteredQuery<>(Person.class, new HashMap<>(), new HashMap<>(), 0, 10),
                new RowMapper(objectMapper));
    assertThat(res.getKey()).hasSize(5);
    assertThat(res.getValue()).isEqualTo(5L);
    PersonValidator.validate(res.getKey());
  }

  @DataTable(value = "person")
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
