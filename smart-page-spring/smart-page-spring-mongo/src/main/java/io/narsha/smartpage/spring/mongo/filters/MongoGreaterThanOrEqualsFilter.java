package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.filters.GreaterThanOrEqualsFilter;
import org.springframework.data.mongodb.core.query.Criteria;

/** JDBC filter for in operation */
public class MongoGreaterThanOrEqualsFilter implements MongoFilter<Object> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return GreaterThanOrEqualsFilter.class;
  }

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).gte(value);
  }
}
