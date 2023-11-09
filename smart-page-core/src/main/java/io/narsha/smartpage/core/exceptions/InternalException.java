package io.narsha.smartpage.core.exceptions;

/** Unique internal exception that will be replaced soon */
@Deprecated
public class InternalException extends RuntimeException {

  /** Default constructor */
  public InternalException() {}

  /**
   * Constructor used when an existing exception was previously thrown
   *
   * @param message custom message
   * @param cause original exception
   */
  public InternalException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public String getMessage() {
    return "Cannot parse the current object";
  }
}
