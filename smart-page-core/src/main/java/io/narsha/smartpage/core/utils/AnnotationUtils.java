package io.narsha.smartpage.core.utils;

import io.narsha.smartpage.core.annotations.DataTableProperty;
import io.narsha.smartpage.core.exceptions.InternalException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AnnotationUtils {

  public static <R, A extends Annotation> R getClassAnnotationValue(
      Class<?> objectClass, Class<A> annotationClass, Function<A, R> function) {
    try {
      final var annotation = objectClass.getAnnotation(annotationClass);
      return function.apply(annotation);
    } catch (Exception e) {
      throw new InternalException();
    }
  }

  public static <R, A extends Annotation> Optional<R> getFieldAnnotationValue(
      Class<?> objectClass, String fieldName, Class<A> annotationClass, Function<A, R> function) {
    try {
      final var field = objectClass.getDeclaredField(fieldName);
      final var annotation = field.getAnnotation(annotationClass);
      return Optional.ofNullable(function.apply(annotation));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  static Optional<String> getQueryProperty(Class<?> objectClass, String javaProperty) {
    try {
      return getFieldAnnotationValue(
          objectClass, javaProperty, DataTableProperty.class, DataTableProperty::columnName);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  static Optional<String> getJavaProperty(Class<?> objectClass, String queryProperty) {
    return Stream.of(objectClass.getDeclaredFields())
        .map(Field::getName)
        .filter(
            javaProperty -> {
              // TODO externalize
              return getQueryProperty(objectClass, javaProperty)
                  .map(e -> e.equalsIgnoreCase(queryProperty))
                  .orElse(javaProperty.equalsIgnoreCase(queryProperty));
            })
        .findFirst();
  }

  public static boolean isAnnotated(
      Class<?> objectClass, String fieldName, Class<? extends Annotation> annotationClass) {
    try {
      final var field = objectClass.getDeclaredField(fieldName);
      return field.getAnnotation(annotationClass) != null;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isFieldExists(Class<?> objectClass, String fieldName) {
    try {
      objectClass.getDeclaredField(fieldName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
