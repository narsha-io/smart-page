package io.narsha.smartpage.core.utils;

import io.narsha.smartpage.core.annotations.DataTableIgnore;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Utils class used to get some information used for query data mapping */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResolverUtils {

  /**
   * Get the property name in the datasource based on the java property in the targetClass
   *
   * @param targetClass DTO class
   * @param javaProperty java attribute's name
   * @return the datasource property if different from the javaProperty
   */
  public static Optional<String> getQueryProperty(Class<?> targetClass, String javaProperty) {
    return Optional.ofNullable(
        AnnotationUtils.getQueryProperty(targetClass, javaProperty)
            .orElseGet(
                () -> {
                  if (AnnotationUtils.isFieldExists(targetClass, javaProperty)) {
                    return javaProperty;
                  }
                  return null;
                }));
  }

  /**
   * Get the property name in the DTO based on the datasource property for the targetClass
   *
   * @param targetClass DTO class
   * @param queryProperty datasource property name
   * @return the datasource property if different from the javaProperty
   */
  public static Optional<String> getJavaProperty(Class<?> targetClass, String queryProperty) {
    return Optional.ofNullable(
        AnnotationUtils.getJavaProperty(targetClass, queryProperty)
            .orElseGet(
                () -> {
                  if (AnnotationUtils.isFieldExists(targetClass, queryProperty)) {
                    return queryProperty;
                  }
                  return null;
                }));
  }

  /**
   * Tell if the java property must be ignored based on the DataTableIgnore annotation
   *
   * @param targetClass DTO class
   * @param property java property
   * @return true if the java property is annotated by DataTableIgnore, otherwise false
   */
  public static boolean isIgnoredField(Class<?> targetClass, String property) {
    return AnnotationUtils.isAnnotated(targetClass, property, DataTableIgnore.class);
  }

  /**
   * return the value of DataTable annotation value property
   *
   * @param <A> annotation class
   * @param <T> method type returned by the function "supplier"
   * @param targetClass DTO class
   * @param annotation the annotation class
   * @param supplier method in order to return the value of a specific method into your annotation
   *     class
   * @return the DataTable.value value
   */
  public static <T, A extends Annotation> T getDataTableValue(
      Class<?> targetClass, Class<A> annotation, Function<A, T> supplier) {
    return AnnotationUtils.getClassAnnotationValue(targetClass, annotation, supplier);
  }
}
