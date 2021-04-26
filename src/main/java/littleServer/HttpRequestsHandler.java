package littleServer;

public interface HttpRequestsHandler {
  String path();

  HttpMethod method();

  HttpResponse process(HttpRequest request);

}
