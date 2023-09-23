package io.narsha.smartpage.core.exceptions;

/** Unique internal exception that will be replaced soon */
@Deprecated
public class InternalException extends RuntimeException {

  @Override
  public String getMessage() {
    return "Cannot parse the current object";
  }
}
