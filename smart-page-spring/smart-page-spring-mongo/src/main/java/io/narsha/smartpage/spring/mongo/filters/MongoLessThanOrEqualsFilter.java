package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.filters.LessThanOrEqualsFilter;
import org.springframework.data.mongodb.core.query.Criteria;

/** JDBC filter for in operation */
public class MongoLessThanOrEqualsFilter implements MongoFilter<Object> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return LessThanOrEqualsFilter.class;
  }

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).lte(value);
  }
}
