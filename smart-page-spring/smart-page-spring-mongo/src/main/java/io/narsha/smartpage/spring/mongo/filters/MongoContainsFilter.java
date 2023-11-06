package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.ContainsFilter;
import java.util.regex.Pattern;
import org.springframework.data.mongodb.core.query.Criteria;

/** Mongo implementation of contains operation */
public class MongoContainsFilter extends ContainsFilter implements MongoFilter {

  @Override
  public Criteria getMongoCriteria(String property, Object value) {
    return Criteria.where(property).regex(Pattern.compile(".*(" + value + ").*"));
  }
}
