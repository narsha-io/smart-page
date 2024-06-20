package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.EqualsFilter;
import org.springframework.data.mongodb.core.query.Criteria;

/** Mongo implementation of equals operation */
public class MongoEqualsFilter extends EqualsFilter implements MongoFilter {

  /** default constructor */
  public MongoEqualsFilter() {}

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).is(value);
  }
}
