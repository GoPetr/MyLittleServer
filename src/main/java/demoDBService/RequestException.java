package demoDBService;

public class RequestException extends Exception {
  public RequestException(String message) {
    super(message);
  }

  public RequestException() {
    super("WRONG!!!");
  }
}
