package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.filters.LessThanFilter;
import org.springframework.data.mongodb.core.query.Criteria;

/** JDBC filter for in operation */
public class MongoLessThanFilter implements MongoFilter<Object> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return LessThanFilter.class;
  }

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).lt(value);
  }
}
