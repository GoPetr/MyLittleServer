package littleServer;

public enum ContentType {
  TEXT_HTML("text/html"),
  APPLICATION_JSON("application/json");

  String contentType;

  ContentType(String contentType) {
    this.contentType = contentType;
  }

  @Override
  public String toString() {
    return contentType;
  }
}
