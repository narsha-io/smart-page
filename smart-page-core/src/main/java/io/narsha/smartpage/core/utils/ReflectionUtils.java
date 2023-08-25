package io.narsha.smartpage.core.utils;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtils {

  public static Optional<Class<?>> getFieldClass(Class<?> targetClass, String property) {
    try {
      for (var field : targetClass.getDeclaredFields()) {
        if (field.getName().equalsIgnoreCase(property)) {
          return Optional.of(field.getType());
        }
      }
      return Optional.empty();
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
