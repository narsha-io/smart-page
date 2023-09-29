package io.narsha.smartpage.core;

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
  <T> SmartPageResult<T> execute(PaginatedFilteredQuery<T> paginatedFilteredQuery);
}
