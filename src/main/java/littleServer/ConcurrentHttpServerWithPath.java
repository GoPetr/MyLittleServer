package littleServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentHttpServerWithPath extends Thread {
  public static final int DEFAULT_SERVER_PORT = 8080;
  int serverPort;
  boolean isALive = true;
  ExecutorService executorService = Executors.newCachedThreadPool();
  public List<HttpRequestsHandler> handlersList = new CopyOnWriteArrayList<>();

  public ConcurrentHttpServerWithPath() {
    this(DEFAULT_SERVER_PORT);
  }

  public ConcurrentHttpServerWithPath(int serverPort) {
    this.serverPort = serverPort;
  }

  public static void main(String[] args) {
    ConcurrentHttpServerWithPath serverThread = new ConcurrentHttpServerWithPath(8080);
    serverThread.start();
    System.out.println("Server started.");

    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.print("Enter \"stop\" to stop the server: ");
      if (scanner.nextLine().equals("stop")) {
        serverThread.stopServer();
        break;
      }
    }
  }

  public void addHandler(HttpRequestsHandler handler) {
    handlersList.add(handler);
  }

  @Override
  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(serverPort);
      while (isLive()) {
        var clientSoket = serverSocket.accept();
        executorService.submit(() -> {
          HttpRequestHandler httpRequestHandler = new HttpRequestHandler(clientSoket);
          httpRequestHandler.executeRequest(handlersList);
        });
      }
    } catch (SocketException e) {
      System.out.println("Server stopped: " + e);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      executorService.shutdown();
    }
  }

  public void stopServer() {
    this.isALive = false;
    System.out.println("Server stopped!");
  }

  public boolean isLive() {
    System.out.println("Inside method: isLive()");
    return this.isALive;
  }
}
