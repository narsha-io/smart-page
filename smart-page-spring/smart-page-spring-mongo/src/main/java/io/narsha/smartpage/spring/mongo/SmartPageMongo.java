package io.narsha.smartpage.spring.mongo;

import io.narsha.smartpage.spring.core.SmartPage;
import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;

public class SmartPageMongo extends SmartPage<List<Criteria>> {
  public SmartPageMongo(MongoQueryExecutor mongoQueryExecutor) {
    super(mongoQueryExecutor);
  }
}
