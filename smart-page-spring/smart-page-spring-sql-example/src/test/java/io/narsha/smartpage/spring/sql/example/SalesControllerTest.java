package io.narsha.smartpage.spring.sql.example;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SalesControllerTest {

  @Autowired private MockMvc mockMvc;

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
}
