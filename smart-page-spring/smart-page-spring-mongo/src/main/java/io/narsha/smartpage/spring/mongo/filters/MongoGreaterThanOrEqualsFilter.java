package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.GreaterThanOrEqualsFilter;
import org.springframework.data.mongodb.core.query.Criteria;

/** JDBC filter for in operation */
public class MongoGreaterThanOrEqualsFilter extends GreaterThanOrEqualsFilter
    implements MongoFilter {

  /** default constructor */
  public MongoGreaterThanOrEqualsFilter() {}

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).gte(value);
  }
}
