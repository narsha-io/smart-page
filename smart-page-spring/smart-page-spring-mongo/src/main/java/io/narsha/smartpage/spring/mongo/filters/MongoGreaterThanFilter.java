package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.filters.GreaterThanFilter;
import org.springframework.data.mongodb.core.query.Criteria;

/** JDBC filter for in operation */
public class MongoGreaterThanFilter implements MongoFilter<Object> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return GreaterThanFilter.class;
  }

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).gt(value);
  }
}
