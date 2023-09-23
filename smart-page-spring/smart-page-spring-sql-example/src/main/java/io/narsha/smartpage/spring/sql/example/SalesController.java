package io.narsha.smartpage.spring.sql.example;

import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.spring.sql.JdbcQueryExecutor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Simple RestController that expose an endpoint to get filtered data */
@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

  private final JdbcQueryExecutor executor;

  /**
   * TODO response entity utils Endpoint to get filtered sales
   *
   * @param query filter to apply
   * @return filtered data
   */
  @GetMapping
  public List<Sales> sales(PaginatedFilteredQuery<Sales> query) {
    return executor.execute(query).getKey();
  }
}
