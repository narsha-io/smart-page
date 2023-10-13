package io.narsha.smartpage.spring.mongo.example;

import io.narsha.smarpage.spring.test.AbstractSmartPageWebSpringTest;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = SmartPageSpringDataExampleApplication.class)
@ContextConfiguration(initializers = SalesControllerTest.Initializer.class)
public class SalesControllerTest extends AbstractSmartPageWebSpringTest {

  @Autowired private MongoTemplate mongoTemplate;

  static boolean init = false;

  @BeforeEach
  void init() {
    if (init) {
      return;
    }
    init = true;
    final String collectionName = "sales_collection";
    mongoTemplate.insert(
        new HashMap(
            Map.of(
                "itemId",
                1L,
                "itemName",
                "T-SHIRT",
                "storeId",
                1L,
                "storeName",
                "PARIS",
                "qty",
                2L)),
        collectionName);
    mongoTemplate.insert(
        new HashMap(
            Map.of(
                "itemId",
                1L,
                "itemName",
                "T-SHIRT",
                "storeId",
                2L,
                "storeName",
                "SEOUL",
                "qty",
                1L)),
        collectionName);
    mongoTemplate.insert(
        new HashMap(
            Map.of(
                "itemId",
                1L,
                "itemName",
                "T-SHIRT",
                "storeId",
                3L,
                "storeName",
                "BEIJING",
                "qty",
                1L)),
        collectionName);
    mongoTemplate.insert(
        new HashMap(
            Map.of(
                "itemId", 3L, "itemName", "CAP", "storeId", 2L, "storeName", "SEOUL", "qty", 1L)),
        collectionName);
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
