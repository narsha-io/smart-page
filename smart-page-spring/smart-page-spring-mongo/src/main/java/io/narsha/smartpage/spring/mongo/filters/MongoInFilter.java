package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.InFilter;
import java.util.Set;
import org.springframework.data.mongodb.core.query.Criteria;

/** Mongo implementation of in operation */
public class MongoInFilter extends InFilter implements MongoFilter {

  /** default constructor */
  public MongoInFilter() {}

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).in(((Set<Object>) value).toArray());
  }
}
