package io.narsha.smartpage.spring.mongo.example;

import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.spring.mongo.SmartPageMongo;
import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Simple RestController that expose an endpoint to get filtered data */
@RestController
@RequestMapping("/api/sales")
public class SalesController {

  private final SmartPageMongo smartPage;

  /**
   * constructor
   *
   * @param smartPage smartPage service
   */
  public SalesController(SmartPageMongo smartPage) {
    this.smartPage = smartPage;
  }

  /**
   * list sales
   *
   * @param query filter to apply
   * @return filtered data
   */
  @GetMapping
  public ResponseEntity<List<Sales>> sales(SmartPageQuery<Sales> query) {
    return smartPage.asResponseEntity(query);
  }

  /**
   * list sales using custom filter
   *
   * @param query filter to apply
   * @return filtered data
   */
  @GetMapping("/custom-filter")
  public ResponseEntity<List<Sales>> salesFilteredByCurrentUser(SmartPageQuery<Sales> query) {
    final var currentUserStore = "SEOUL"; // SecurityContext.getCurrentUserStore();
    final var storeNameCriteria = Criteria.where("storeName").is(currentUserStore);
    return smartPage.asResponseEntity(query, List.of(storeNameCriteria));
  }
}
