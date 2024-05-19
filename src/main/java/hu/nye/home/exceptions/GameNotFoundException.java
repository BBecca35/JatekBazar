package hu.nye.home.exceptions;

/**
 * Exception thrown when a game is not found.
 */
public class GameNotFoundException extends Exception {
  
  public GameNotFoundException() {
    super();
  }
  
  public GameNotFoundException(String message) {
    super(message);
  }
  
  public GameNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public GameNotFoundException(Throwable cause) {
    super(cause);
  }
}
