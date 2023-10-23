## RestfulAPIServer
A brief and simple Restful API server library in Java

### Documentation:
+ [**Quick Start**]([https://github.com/Blocker45/RestfulAPIServer/doc/QuickStart.md](https://github.com/Blocker45/RestfulAPIServer/blob/main/doc/QuickStart.md))

### Features:
#### 1.No extra dependencies
This project only used core libraries in jdk and requires no more extra dependencies.
#### 2.Simple
You start a Restful API server with just several lines of codes like:
```
public static void main(String[] args) throws IOException {

    int port = 8080;
    String path = "/Web/";

    webserver = new RestfulAPIServer(port);
    webserver.setPath("./web/"); // Set the path for static resources

    webserver.GET("/test", new APIHandler() { // Setup a new API handler for Get requests at "./test"
        @Override
        public void handle(Context context) {
            Map<String, String> params = context.getParams(); // Get the params from the request
            context.Text("Server is running, your params:" + params.toString()); // Respond text to the client
        }
    });

    webserver.start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        webserver.stop();
    }));
}
```
#### 3.Efficient
Cocurrent requests supported.

Copyright@**Blocker45**
