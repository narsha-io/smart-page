package io.narsha.smartpage.web.test;

import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.params.provider.Arguments;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractSmartPageWebTest {

  protected static Stream<Arguments> webSource() {
    return Stream.of(
        Arguments.of(
            "allData",
            "/api/sales",
            "<http://localhost/api/sales?page=0>; rel=\"prev\",<http://localhost/api/sales?page=0>; rel=\"first\",",
            "4",
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
                                                """),
        Arguments.of(
            "paginatedDataFirstPage",
            "/api/sales?page=0&size=2",
            "<http://localhost/api/sales?page=1&size=2>; rel=\"next\",<http://localhost/api/sales?page=1&size=2>; rel=\"prev\",<http://localhost/api/sales?page=0&size=2>; rel=\"first\",",
            "4",
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
                        """),
        Arguments.of(
            "paginatedDataSecondPage",
            "/api/sales?page=1&size=2",
            "<http://localhost/api/sales?page=1&size=2>; rel=\"prev\",<http://localhost/api/sales?page=0&size=2>; rel=\"first\",",
            "4",
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
                        """),
        Arguments.of(
            "testSortAsc",
            "/api/sales?sort=itemId,asc",
            "<http://localhost/api/sales?sort=itemId,asc&page=0>; rel=\"prev\",<http://localhost/api/sales?sort=itemId,asc&page=0>; rel=\"first\",",
            "4",
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
                        """),
        Arguments.of(
            "testSortDesc",
            "/api/sales?sort=storeId,desc",
            "<http://localhost/api/sales?sort=storeId,desc&page=0>; rel=\"prev\",<http://localhost/api/sales?sort=storeId,desc&page=0>; rel=\"first\",",
            "4",
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
                        """),
        Arguments.of(
            "testSortWithRename",
            "/api/sales?sort=quantity,desc",
            "<http://localhost/api/sales?sort=quantity,desc&page=0>; rel=\"prev\",<http://localhost/api/sales?sort=quantity,desc&page=0>; rel=\"first\",",
            "4",
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
                         """),
        Arguments.of(
            "testFilterWithRename",
            "/api/sales?quantity=2",
            "<http://localhost/api/sales?quantity=2&page=0>; rel=\"prev\",<http://localhost/api/sales?quantity=2&page=0>; rel=\"first\",",
            "1",
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
