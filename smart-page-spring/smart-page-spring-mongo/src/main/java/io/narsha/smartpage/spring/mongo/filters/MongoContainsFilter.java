package io.narsha.smartpage.spring.mongo.filters;

import io.narsha.smartpage.core.filters.ContainsFilter;
import io.narsha.smartpage.core.filters.FilterParser;
import java.util.regex.Pattern;
import org.springframework.data.mongodb.core.query.Criteria;

public class MongoContainsFilter implements MongoFilter<String> {
  @Override
  public Class<? extends FilterParser> getParserType() {
    return ContainsFilter.class;
  }

  @Override
  public Criteria getMongoCriteria(String property, String value) {
    return Criteria.where(property).regex(Pattern.compile(".*(" + value + ").*"));
  }
}
