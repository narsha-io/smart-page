package io.narsha.smartpage.spring.mongo.example;

import io.narsha.smartpage.core.SmartPageQuery;
import io.narsha.smartpage.spring.core.SmartPage;
import java.util.List;
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

  private final SmartPage smartPage;

  /**
   * TODO response entity utils Endpoint to get filtered sales
   *
   * @param query filter to apply
   * @return filtered data
   */
  @GetMapping
  public ResponseEntity<List<Sales>> sales(SmartPageQuery<Sales> query) {
    return smartPage.asResponseEntity(query);
  }
}
