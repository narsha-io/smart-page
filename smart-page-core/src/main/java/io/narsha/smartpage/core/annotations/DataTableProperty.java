package io.narsha.smartpage.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Use this class to apply a mapping between your datasource and your DTO */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTableProperty {

  /**
   * the datasource property name corresponding to the attribute where this annotation is set
   *
   * @return the datasource property name
   */
  String columnName();
}
