package io.narsha.smartpage.spring.mongo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Use this interface on the java class you want to use as a smart-page data */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoDataTable {

  /**
   * the mongo collection name
   *
   * @return the collection name
   */
  String collection();
}
