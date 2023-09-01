package io.narsha.smartpage.sprng.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.narsha.smartpage.core.PaginatedFilteredQuery;
import io.narsha.smartpage.core.RowMapper;
import io.narsha.smartpage.spring.data.JdbcQueryExecutor;
import io.narsha.smartpage.spring.test.SmartPageSpringTestApplication;
import io.narsha.smartpage.spring.test.model.Person;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(classes = {SmartPageSpringTestApplication.class})
public class JdbcQueryExecutorTest {

  @Autowired private JdbcTemplate jdbcTemplate;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void test() {
    final Pair<List<Person>, Long> res =
        new JdbcQueryExecutor(jdbcTemplate, null)
            .execute(
                new PaginatedFilteredQuery<>(Person.class, 0, 10), new RowMapper(objectMapper));
    res.getKey().forEach(System.out::println);
  }
}
