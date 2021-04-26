package littleServer;

public enum HttpVersion {
  HTTP_1_1("HTTP/1.1");

  String httpVersion;

  HttpVersion(String httpVersion) {
    this.httpVersion = httpVersion;
  }

  @Override
  public String toString() {
    return httpVersion;
  }
}
