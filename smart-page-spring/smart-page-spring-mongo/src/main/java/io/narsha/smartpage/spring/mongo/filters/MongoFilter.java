package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.Filter;
import org.springframework.data.mongodb.core.query.Criteria;

/** Mongo filter interface */
public interface MongoFilter extends Filter {

  /**
   * Get the corresponding mongo criteria
   *
   * @param property name of the field you want to filter
   * @param value filter value
   * @return the mongo criteria
   */
  Criteria getMongoCriteria(String property, Object value);
}
