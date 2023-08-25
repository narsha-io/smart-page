package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.spring.core.filters.Filter;
import org.springframework.data.mongodb.core.query.Criteria;

public interface MongoFilter<T> extends Filter<T> {

  Criteria getMongoCriteria(String property, T value);
}
