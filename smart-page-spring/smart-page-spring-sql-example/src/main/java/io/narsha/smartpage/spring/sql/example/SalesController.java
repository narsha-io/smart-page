package io.narsha.smartpage.spring.sql.example;

import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.spring.sql.SmartPageJdbc;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Simple RestController that expose an endpoint to get filtered data */
@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

  private final SmartPageJdbc smartPage;

  /**
   * @param query filter to apply
   * @return filtered data
   */
  @GetMapping
  public ResponseEntity<List<Sales>> sales(SmartPageQuery<Sales> query) {
    return smartPage.asResponseEntity(query);
  }

  /**
   * @param query filter to apply
   * @return filtered data
   */
  @GetMapping("/custom-filter")
  public ResponseEntity<List<SalesWithFilteredQuery>> salesFilteredByCurrentUser(
      SmartPageQuery<SalesWithFilteredQuery> query) {
    final var currentUserStore = "SEOUL"; // SecurityContext.getCurrentUserStore();
    return smartPage.asResponseEntity(query, Map.of("storeName", currentUserStore));
  }
}
