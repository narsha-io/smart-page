package io.narsha.smartpage.core;

/** Interface that give a query data for a paginatedfilterQuery */
public interface QueryExecutor {

  // TODO replace Pair by record
  /**
   * Execute the query
   *
   * @param paginatedFilteredQuery query information needed to execute the query
   * @return the query data
   * @param <T> type of the DTO data
   */
  <T> SmartPageResult<T> execute(SmartPageQuery<T> paginatedFilteredQuery);
}
