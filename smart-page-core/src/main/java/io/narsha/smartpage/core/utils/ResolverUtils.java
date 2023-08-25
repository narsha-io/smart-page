package io.narsha.smartpage.core.utils;

import io.narsha.smartpage.core.annotations.DataTable;
import io.narsha.smartpage.core.annotations.DataTableIgnore;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResolverUtils {

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

  public static boolean isIgnoredField(Class<?> targetClass, String property) {
    return AnnotationUtils.isAnnotated(targetClass, property, DataTableIgnore.class);
  }

  public static String getDataTableValue(Class<?> targetClass) {
    return AnnotationUtils.getClassAnnotationValue(targetClass, DataTable.class, DataTable::value);
  }
}
