package io.narsha.smartpage.core;

import lombok.Getter;

@Getter
public class QueryBuilder {

  private StringBuilder dataQuery = new StringBuilder();
  private StringBuilder countQuery = new StringBuilder();
}
