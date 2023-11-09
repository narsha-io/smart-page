package io.narsha.smartpage.core;

/**
 * Interface that give a query data for a paginatedfilterQuery
 *
 * @param <P> type of extra parameters
 */
public interface QueryExecutor<P> {

  /**
   * Execute the query
   *
   * @param paginatedFilteredQuery query information needed to execute the query
   * @param extraParameters use this if you want to apply some extra parameters to your query
   * @param <T> type of the DTO data
   * @return the query data
   */
  <T> SmartPageResult<T> execute(SmartPageQuery<T> paginatedFilteredQuery, P extraParameters);
}
