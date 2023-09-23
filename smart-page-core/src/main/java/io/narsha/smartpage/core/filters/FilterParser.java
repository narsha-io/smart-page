package io.narsha.smartpage.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Abstract class which provide some method to parse String array into a targetClass
 *
 * @param <T> the target property class
 * @param <R> the final value class
 */
public abstract class FilterParser<T, R> {

  /** the target property class */
  protected Class<T> targetClass;

  /**
   * Basic constructor
   *
   * @param targetClass the target property class
   */
  protected FilterParser(Class<T> targetClass) {
    this.targetClass = targetClass;
  }

  /**
   * Method that will parse the parameter value into the targetClass using the objectMapper
   * parameter
   *
   * @param objectMapper jackson object mapper used to convert the parameter value into the class
   *     attribute targetclass
   * @param value the value
   */
  public abstract void parse(ObjectMapper objectMapper, String[] value);

  /**
   * Get the final result
   *
   * @return the parsed value
   */
  public abstract R getValue();
}
