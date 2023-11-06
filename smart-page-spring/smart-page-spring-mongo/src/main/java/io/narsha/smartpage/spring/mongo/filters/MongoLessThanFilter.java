package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.LessThanFilter;
import org.springframework.data.mongodb.core.query.Criteria;

/** JDBC filter for in operation */
public class MongoLessThanFilter extends LessThanFilter implements MongoFilter {

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).lt(value);
  }
}
