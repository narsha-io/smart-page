package io.narsha.smartpage.spring.mongo.example;

import io.narsha.smartpage.spring.test.AbstractSmartPageWebSpringTest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
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
    Stream.of(
            new Object[] {1L, "T-SHIRT", 1L, "PARIS", 2L},
            new Object[] {1L, "T-SHIRT", 2L, "SEOUL", 1L},
            new Object[] {1L, "T-SHIRT", 3L, "BEIJING", 1L},
            new Object[] {3L, "CAP", 2L, "SEOUL", 1L})
        .forEach(
            tab -> {
              mongoTemplate.insert(
                  new HashMap<>(
                      Map.of(
                          "itemId",
                          tab[0],
                          "itemName",
                          tab[1],
                          "storeId",
                          tab[2],
                          "storeName",
                          tab[3],
                          "qty",
                          tab[4])),
                  collectionName);
            });
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
