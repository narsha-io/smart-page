package io.narsha.smartpage.spring.mongo.example;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = SmartPageSpringDataExampleApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = SalesControllerTest.Initializer.class)
public class SalesControllerTest {

  @Autowired private MockMvc mockMvc;

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

  @Test
  void allData() throws Exception {
    this.mockMvc
        .perform(get("/api/sales"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    """
                                                [{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":1,
                                                    "storeName":"PARIS",
                                                    "quantity":2
                                                  },{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":2,
                                                    "storeName":"SEOUL",
                                                    "quantity":1
                                                  },{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":3,
                                                    "storeName":"BEIJING",
                                                    "quantity":1
                                                  },{
                                                    "itemId":3,
                                                    "itemName":"CAP",
                                                    "storeId":2,
                                                    "storeName":"SEOUL",
                                                    "quantity":1
                                                  }]
                                                """,
                    true));
  }

  @Test
  void paginatedDataFirstPage() throws Exception {
    this.mockMvc
        .perform(get("/api/sales?page=0&size=2"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    """
                                                [{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":1,
                                                    "storeName":"PARIS",
                                                    "quantity":2
                                                  },{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":2,
                                                    "storeName":"SEOUL",
                                                    "quantity":1
                                                  }]
                                                """,
                    true));
  }

  @Test
  void paginatedDataSecondPage() throws Exception {
    this.mockMvc
        .perform(get("/api/sales?page=1&size=2"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    """
                                                [{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":3,
                                                    "storeName":"BEIJING",
                                                    "quantity":1
                                                  },{
                                                    "itemId":3,
                                                    "itemName":"CAP",
                                                    "storeId":2,
                                                    "storeName":"SEOUL",
                                                    "quantity":1
                                                  }]
                                                """,
                    true));
  }

  @Test
  void testSortAsc() throws Exception {
    this.mockMvc
        .perform(get("/api/sales?sort=itemId,asc"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    """
                                                [{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":1,
                                                    "storeName":"PARIS",
                                                    "quantity":2
                                                  },{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":2,
                                                    "storeName":"SEOUL",
                                                    "quantity":1
                                                  },{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":3,
                                                    "storeName":"BEIJING",
                                                    "quantity":1
                                                  },{
                                                    "itemId":3,
                                                    "itemName":"CAP",
                                                    "storeId":2,
                                                    "storeName":"SEOUL",
                                                    "quantity":1
                                                  }]
                                                """,
                    true));
  }

  @Test
  void testSortDesc() throws Exception {
    this.mockMvc
        .perform(get("/api/sales?sort=storeId,desc"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    """
                                                [{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":3,
                                                    "storeName":"BEIJING",
                                                    "quantity":1
                                                 },{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":2,
                                                    "storeName":"SEOUL",
                                                    "quantity":1
                                                  },{
                                                    "itemId":3,
                                                    "itemName":"CAP",
                                                    "storeId":2,
                                                    "storeName":"SEOUL",
                                                    "quantity":1
                                                  },{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":1,
                                                    "storeName":"PARIS",
                                                    "quantity":2
                                                  }
                                                ]
                                                """,
                    true));
  }

  @Test
  void testSortWithRename() throws Exception {
    this.mockMvc
        .perform(get("/api/sales?sort=quantity,desc"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    """
                                                 [{
                                                 "itemId":1,
                                                 "itemName":"T-SHIRT",
                                                 "storeId":1,
                                                 "storeName":"PARIS",
                                                 "quantity":2
                                                 },{
                                                 "itemId":1,
                                                 "itemName":"T-SHIRT",
                                                 "storeId":2,
                                                 "storeName":"SEOUL",
                                                 "quantity":1
                                                },{
                                                "itemId":1,
                                                "itemName":"T-SHIRT",
                                                "storeId":3,
                                                "storeName":"BEIJING",
                                                "quantity":1
                                                },{
                                                "itemId":3,
                                                "itemName":"CAP",
                                                "storeId":2,
                                                "storeName":"SEOUL",
                                                "quantity":1
                                                }
                                                 ]
                                                 """,
                    true));
  }

  @Test
  void testFilterWithRename() throws Exception {
    this.mockMvc
        .perform(get("/api/sales?quantity=2"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    """
                                                [{
                                                    "itemId":1,
                                                    "itemName":"T-SHIRT",
                                                    "storeId":1,
                                                    "storeName":"PARIS",
                                                    "quantity":2
                                                  }
                                                  ]"""));
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
