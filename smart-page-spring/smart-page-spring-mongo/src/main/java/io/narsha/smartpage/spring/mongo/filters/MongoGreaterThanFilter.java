package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.GreaterThanFilter;
import org.springframework.data.mongodb.core.query.Criteria;

/** JDBC filter for in operation */
public class MongoGreaterThanFilter extends GreaterThanFilter implements MongoFilter {

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).gt(value);
  }
}
