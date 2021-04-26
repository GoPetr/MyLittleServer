package littleServer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class HttpRequestHandler {
  Socket clientSocket;

  public HttpRequestHandler(Socket socket) {
    this.clientSocket = socket;
  }

  public void executeRequest(List<HttpRequestsHandler> handlerList) {
    try (BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
         OutputStream out = clientSocket.getOutputStream()) {
      StringBuilder lineRequest = new StringBuilder();
      String line;
      while ((line = in.readLine()) != null && !line.isBlank()) {
        lineRequest.append(line);
        lineRequest.append("\n\r");
      }

      String body = "";
      if (in.ready()) {
        char[] buffer = new char[119]; // WARNING! Need TODO
        int charCount = in.read(buffer, 0, 119);
        body = String.valueOf(buffer);
        System.out.println("Request body: " + body);
      }

      String[] splitRequest = lineRequest.toString().split(" ");

      for (int i = 0; i < splitRequest.length; i++) {
        System.out.print("Index number: ");
        System.out.println(i + " " + splitRequest[i]);
      }

      ContentType contentType = switch (splitRequest[0]) {
        case "POST" -> ContentType.APPLICATION_JSON;
        default -> ContentType.TEXT_HTML;
      };

      HttpRequest httpRequest = new HttpRequest.Builder()
              .path(splitRequest[1])
              .method(HttpMethod.valueOf(splitRequest[0]))
              .body(body)
              .contentType(contentType)
              .httpVersion(HttpVersion.HTTP_1_1)
              .build();

      System.out.println("HttpRequest: " + httpRequest.toString());

      HttpResponse response = null;

      for (int i = 0; i < handlerList.size(); i++) {
        if (httpRequest.path().equals(handlerList.get(i).path())
                && httpRequest.httpMethod().equals(handlerList.get(i).method())) {
          response = handlerList.get(i).process(httpRequest);
          break;
        }
      }

      int contentLength = 0;
      if (response != null && response.body() != null) {
        contentLength = response.body().length() + 4;
      }

      if (response != null) {
        out.write((response.httpVersion() + " " + response.status() + "\r\n").getBytes());
        out.write(("Content-Length: " + contentLength + "\r\n").getBytes());
        out.write(("Content-Type: " + response.contentType() + "\r\n\r\n").getBytes());
        out.write((response.body() + "\r\n\r\n").getBytes());
        System.out.println("RESPONSE: " + response);
      } else {
        response = HttpResponse.ERROR_404;
        out.write((response.httpVersion() + " " + HttpResponse.ResponseStatus.ERROR_404 + "\r\n").getBytes());
        out.write(("Content-Length: " + contentLength + "\r\n").getBytes());
        out.write(("Content-Type: " + response.contentType() + "\r\n\r\n").getBytes());
        out.write((response.body() + "\r\n\r\n").getBytes());
        System.out.println("RESPONSE: " + response);
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
