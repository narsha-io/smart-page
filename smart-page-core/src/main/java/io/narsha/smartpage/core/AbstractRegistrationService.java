package io.narsha.smartpage.core;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractRegistrationService<T> {

  protected final Set<T> registeredService = new HashSet<>();

  public void register(T service) {
    this.registeredService.add(service);
  }
}
