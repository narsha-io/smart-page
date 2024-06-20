package io.narsha.smartpage.core.utils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/** Utils class needed to parse a class using reflection */
@Slf4j
public class ReflectionUtils {

  /** default constructor */
  private ReflectionUtils() {}

  /**
   * Get the class of a property in a targetClass
   *
   * @param targetClass DTO class
   * @param property property name
   * @return property class
   */
  public static Optional<Class<?>> getFieldClass(Class<?> targetClass, String property) {
    try {
      for (var field : getDeclaredFields(targetClass)) {
        if (field.getName().equalsIgnoreCase(property)) {
          return Optional.of(field.getType());
        }
      }
      return Optional.empty();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Optional.empty();
    }
  }

  /**
   * Return all fields and parent class fields
   *
   * @param targetClass the class which has to be parsed
   * @return all class and parent fields
   */
  public static Set<Field> getDeclaredFields(Class<?> targetClass) {
    return org.reflections.ReflectionUtils.getAllFields(targetClass);
  }

  /**
   * Return all fields and parent class fields
   *
   * @param targetClass the class which has to be parsed
   * @param property the class property name
   * @return all class and parent fields
   */
  public static Optional<Field> getDeclaredField(Class<?> targetClass, String property) {
    return getDeclaredFields(targetClass).stream()
        .filter(e -> e.getName().equalsIgnoreCase(property))
        .findFirst();
  }
}
