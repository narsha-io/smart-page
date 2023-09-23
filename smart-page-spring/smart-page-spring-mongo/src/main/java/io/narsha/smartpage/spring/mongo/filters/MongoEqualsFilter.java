package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.EqualsFilter;
import io.narsha.smartpage.core.filters.FilterParser;
import org.springframework.data.mongodb.core.query.Criteria;

/** Mongo implementation of equals operation */
public class MongoEqualsFilter implements MongoFilter<Object> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return EqualsFilter.class;
  }

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).is(value);
  }
}
