package io.narsha.smartpage.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Use this interface on the java class you want to use as a smart-page result */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTable {

  /**
   * Information to provide to smart-page sql query mongo collection
   *
   * @return your configuration
   */
  String value();
}
