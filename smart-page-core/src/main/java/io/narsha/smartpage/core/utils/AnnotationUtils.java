package io.narsha.smartpage.core.utils;

import io.narsha.smartpage.core.annotations.DataTableProperty;
import io.narsha.smartpage.core.exceptions.InternalException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
class AnnotationUtils {

  public static <R, A extends Annotation> R getClassAnnotationValue(
      Class<?> objectClass, Class<A> annotationClass, Function<A, R> function) {
    try {
      final var annotation = objectClass.getAnnotation(annotationClass);
      return function.apply(annotation);
    } catch (Exception e) {
      log.error("Error while parsing the current object : " + e.getMessage(), e);
      throw new InternalException();
    }
  }

  public static <R, A extends Annotation> Optional<R> getFieldAnnotationValue(
      Class<?> objectClass, String fieldName, Class<A> annotationClass, Function<A, R> function) {
    try {
      return ReflectionUtils.getDeclaredField(objectClass, fieldName)
          .map(field -> field.getAnnotation(annotationClass))
          .map(function);
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
    return ReflectionUtils.getDeclaredFields(objectClass).stream()
        .map(Field::getName)
        .filter(javaProperty -> isValidProperty(objectClass, javaProperty, queryProperty))
        .findFirst();
  }

  private static Boolean isValidProperty(
      Class<?> objectClass, String javaProperty, String queryProperty) {
    return getQueryProperty(objectClass, javaProperty)
        .map(e -> e.equalsIgnoreCase(queryProperty))
        .orElse(javaProperty.equalsIgnoreCase(queryProperty));
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
      return io.narsha.smartpage.core.utils.ReflectionUtils.getDeclaredFields(objectClass).stream()
          .map(Field::getName)
          .map(String::toUpperCase)
          .anyMatch(e -> Objects.equals(e, fieldName.toUpperCase()));
    } catch (Exception e) {
      return false;
    }
  }
}
