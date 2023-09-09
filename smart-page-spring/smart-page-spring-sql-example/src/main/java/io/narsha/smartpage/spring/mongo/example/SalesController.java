package io.narsha.smartpage.spring.mongo.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.spring.sql.JdbcQueryExecutor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

  @Autowired private JdbcQueryExecutor executor;

  /**
   * TODO response entity utils
   *
   * @return
   */
  @GetMapping
  public List<Sales> sales(PaginatedFilteredQuery<Sales> query) {
    // TODO rowMapper as a been inside executor
    return executor.execute(query, new RowMapper(new ObjectMapper())).getKey();
  }
}
