package littleServer;


public record HttpResponse(ResponseStatus status,
                           ContentType contentType,
                           String body,
                           HttpVersion httpVersion) {

  public static final HttpResponse ERROR_404 = new Builder().status(ResponseStatus.ERROR_404).build();
  public static final HttpResponse OK_200 = new Builder().status(ResponseStatus.OK).build();
  public static final HttpResponse ERROR_500 = new Builder().status(ResponseStatus.ERROR_500).build();

  public static class Builder {

    private ResponseStatus status;
    private ContentType contentType;
    private String body;
    private HttpVersion httpVersion;

    public Builder status(ResponseStatus status) {
      this.status = status;
      return this;
    }

    public Builder contentType(ContentType contentType) {
      this.contentType = contentType;
      return this;
    }

    public Builder body(String body) {
      this.body = body;
      return this;
    }

    public Builder httpVersion(HttpVersion version) {
      this.httpVersion = version;
      return this;
    }

    public HttpResponse build() {

      return new HttpResponse(this.status == null ? ResponseStatus.OK : this.status,
              this.contentType == null ? ContentType.TEXT_HTML : this.contentType,
              this.body,
              this.httpVersion == null ? HttpVersion.HTTP_1_1 : this.httpVersion);
    }
  }

  public enum ResponseStatus {
    OK(200, "OK"),
    ERROR_404(404, "not found"),
    ERROR_500(500, "server error");

    int responseCode;
    String responseValue;

    ResponseStatus(int responseCode, String responseValue) {
      this.responseCode = responseCode;
      this.responseValue = responseValue;
    }

    @Override
    public String toString() {
      return this.responseCode + " " + this.responseValue;
    }
  }
}

