package io.narsha.smartpage.core.utils;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Utils class needed to parse a class using reflection */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtils {

  /**
   * Get the class of a property in a targetClass
   *
   * @param targetClass DTO class
   * @param property property name
   * @return property class
   */
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
