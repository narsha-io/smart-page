package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.FilterParser;
import io.narsha.smartpage.core.filters.InFilter;
import java.util.Set;
import org.springframework.data.mongodb.core.query.Criteria;

/** Mongo implementation of in operation */
public class MongoInFilter implements MongoFilter<Set<Object>> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return InFilter.class;
  }

  @Override
  public Criteria getMongoCriteria(String property, Set<Object> value) {
    return Criteria.where(property).in(value.toArray());
  }
}
