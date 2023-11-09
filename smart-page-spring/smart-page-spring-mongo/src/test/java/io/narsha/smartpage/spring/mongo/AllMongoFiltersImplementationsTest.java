package io.narsha.smartpage.spring.mongo;

import io.narsha.smartpage.spring.mongo.filters.MongoFilter;
import io.narsha.smartpage.web.AllFiltersImplementationsTest;

public class AllMongoFiltersImplementationsTest extends AllFiltersImplementationsTest {

  public AllMongoFiltersImplementationsTest() {
    super(MongoFilter.class);
  }
}
