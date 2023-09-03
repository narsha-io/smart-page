package io.narsha.smartpage.spring.mongo;

import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.annotations.DataTableIgnore;
import io.narsha.smartpage.core.annotations.DataTableProperty;
import io.narsha.smartpage.spring.test.SmartPageSpringTestApplication;
import java.util.Map;
import org.bson.Document;
import org.junit.jupiter.api.MethodOrderer;
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

  @Test
  void test() {
    mongoTemplate.getDb().createCollection("person");
    var res =
        mongoTemplate
            .getDb()
            .getCollection("person")
            .insertOne(new Document(Map.of("first_name", "cyril")));
    System.out.println(res.getInsertedId());
  }

  @DataTable(value = "person")
  private static class Person {

    private Long id;

    @DataTableProperty(columnName = "first_name")
    private String firstName;

    @DataTableIgnore private String role;
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
