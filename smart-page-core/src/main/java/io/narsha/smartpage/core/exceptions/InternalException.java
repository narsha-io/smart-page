package io.narsha.smartpage.core.exceptions;

public class InternalException extends RuntimeException {

  @Override
  public String getMessage() {
    return "Cannot parse the current object";
  }
}
