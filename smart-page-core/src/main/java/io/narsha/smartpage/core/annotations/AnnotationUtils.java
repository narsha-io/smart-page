package io.narsha.smartpage.core.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotationUtils {

  public static Class<?> getClass(Class<?> objectClass, String property) {
    return Stream.of(objectClass.getDeclaredFields())
        .filter(f -> f.getName().equals(property))
        .map(Field::getType)
        .findFirst()
        .orElseThrow(); // TODO exception
  }

  public static <R, A extends Annotation> R getAnnotationProperty(
      Class<?> objectClass, Class<A> annotationClass, Function<A, R> function) {
    try {
      final var annotation = objectClass.getAnnotation(annotationClass);
      return function.apply(annotation);
    } catch (Exception e) {
      throw new RuntimeException(); // TODO Exception
    }
  }

  public static <R, A extends Annotation> R getAnnotationProperty(
      Class<?> objectClass, String fieldName, Class<A> annotationClass, Function<A, R> function) {
    try {
      final var field = objectClass.getDeclaredField(fieldName);
      final var annotation = field.getAnnotation(annotationClass);
      return function.apply(annotation);
    } catch (Exception e) {
      throw new RuntimeException(); // TODO Exception
    }
  }

  public static String getQueryProperty(Class<?> objectClass, String javaProperty) {
    try {
      return getAnnotationProperty(
          objectClass, javaProperty, DataTableProperty.class, DataTableProperty::columnName);
    } catch (Exception e) {
      return javaProperty;
    }
  }

  public static String getJavaProperty(Class<?> objectClass, String queryProperty) {
    try {
      return Stream.of(objectClass.getDeclaredFields())
          .map(Field::getName)
          .filter(
              javaProperty ->
                  getQueryProperty(objectClass, javaProperty).equalsIgnoreCase(queryProperty))
          .findFirst()
          .orElse(queryProperty);
    } catch (Exception e) {
      return queryProperty;
    }
  }
}
