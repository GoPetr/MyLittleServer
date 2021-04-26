package littleServer;

import java.util.Optional;

public record HttpRequest(String path,
                          HttpMethod httpMethod,
                          Optional<String> body,
                          ContentType contentType,
                          HttpVersion httpVersion) {

  public static class Builder {

    String path;
    HttpMethod httpMethod;
    Optional<String> body;
    ContentType contentType;
    HttpVersion httpVersion;

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder method(HttpMethod method) {
      httpMethod = method;
      return this;
    }

    public Builder body(String body) {
      this.body = Optional.ofNullable(body);
      return this;
    }

    public Builder contentType(ContentType contentType) {
      this.contentType = contentType;
      return this;
    }

    public Builder httpVersion(HttpVersion httpVersion) {
      this.httpVersion = httpVersion;
      return this;
    }

    public HttpRequest build() {
      return new HttpRequest(this.path == null ? "/" : this.path,
              this.httpMethod == null ? HttpMethod.GET : this.httpMethod,
              this.body,
              this.contentType == null ? ContentType.TEXT_HTML : this.contentType,
              this.httpVersion == null ? HttpVersion.HTTP_1_1 : this.httpVersion);
    }
  }
}
