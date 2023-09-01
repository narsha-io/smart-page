package io.narsha.smartpage.core;

public class QueryBuilder {

  private StringBuilder dataQuery = new StringBuilder();
  private StringBuilder countQuery = new StringBuilder();

  public String getDataQuery() {
    return this.dataQuery.toString();
  }

  public String getCountQuery() {
    return this.countQuery.toString();
  }
}
