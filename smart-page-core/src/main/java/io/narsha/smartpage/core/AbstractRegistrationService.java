package io.narsha.smartpage.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Collect in a Set all object passed as parameter in the method register
 *
 * @param <T> Kind of object that we want to register
 */
public abstract class AbstractRegistrationService<T> {

  /** Set to contain as unique all instance of T objects */
  protected final Set<T> registeredService = new HashSet<>();

  /**
   * Save the object service
   *
   * @param service object that we want to keep in the registeredService list
   */
  public void register(T service) {
    this.registeredService.add(service);
  }
}
