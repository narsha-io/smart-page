package io.narsha.smartpage.core;

import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

/** Interface that give a query result for a paginatedfilterQuery */
public interface QueryExecutor {

  // TODO replace Pair by record
  /**
   * Execute the query
   *
   * @param paginatedFilteredQuery query information needed to execute the query
   * @return the query result
   * @param <T> type of the DTO result
   */
  <T> Pair<List<T>, Long> execute(PaginatedFilteredQuery<T> paginatedFilteredQuery);
}
