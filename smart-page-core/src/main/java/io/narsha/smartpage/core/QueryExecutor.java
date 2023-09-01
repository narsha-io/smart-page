package io.narsha.smartpage.core;

import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public interface QueryExecutor {

  <T> Pair<List<T>, Long> execute(
      PaginatedFilteredQuery<T> paginatedFilteredQuery, RowMapper rowMapper);
}
