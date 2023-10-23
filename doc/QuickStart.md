# QuickStart Guide for RestfulAPIServer

Welcome to the QuickStart guide for the `RestfulAPIServer` package, a simple Java library for creating RESTful APIs. This guide will walk you through the basic steps to get started with this library.

## Installation

Before you can use the `RestfulAPIServer`, make sure you have the required setup in place.

1. Ensure you have Java installed on your system.

2. Download the `RestfulAPIServer` package and add it to your project's classpath.

## Getting Started

Now that you have the package installed, let's create a simple RESTful API server.

```java
import xyz.Blockers.Utils.RestfulAPIServer.RestfulAPIServer;
import xyz.Blockers.Utils.RestfulAPIServer.APIHandler;

public class MyAPIServer {

    public static void main(String[] args) {
        int port = 8080; // Choose your desired port

        // Create the RESTful API server
        RestfulAPIServer server;
        try {
            server = new RestfulAPIServer(port);
        } catch (IOException e) {
            System.err.println("Failed to create the server: " + e.getMessage());
            return;
        }

        // Start the server
        if (server.start()) {
            System.out.println("Server started on port " + port);
        } else {
            System.err.println("Server could not be started.");
            return;
        }

        // Define and register your API endpoints
        server.GET("/api/hello", (exchange) -> {
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write("Hello, World!".getBytes());
            os.close();
        });

        server.POST("/api/greet", (exchange) -> {
            // Handle the POST request
            // ...
        });

        // To stop the server gracefully, use server.stop()
    }
}
```
In the code above, we create a simple RESTful API server that listens on port 8080 and defines two endpoints: a GET endpoint at `/api/hello` and a POST endpoint at `/api/greet`.

## API Endpoint Definitions

The `RestfulAPIServer` provides two methods for defining API endpoints: `GET` and `POST`. Here's how you can use them:

### `GET` Endpoint
```java
server.GET("/path", (exchange) -> {
    // Handle the GET request
    // ...
});
```

### `POST` Endpoint
```java
server.POST("/path", (exchange) -> {
    // Handle the POST request
    // ...
});
```

Replace `/path` with the desired endpoint path and provide the code to handle the request inside the lambda function.

## Customization

You can customize the base path of your server using the `setPath` method. For example:
```java
server.setPath("/myapi");
```

This will change the base path of your server to `/myapi`.

## Starting and Stopping the Server

To start the server, use the `start` method:
```java
server.start();
```

To stop the server gracefully, use the `stop` method:
```java
server.stop();
```