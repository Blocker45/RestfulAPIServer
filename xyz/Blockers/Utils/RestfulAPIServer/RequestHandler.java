package xyz.Blockers.Utils.RestfulAPIServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class RequestHandler implements HttpHandler {
    private String path;
    private Map<String, APIContext> POSTAPIList;
    private Map<String, APIContext> GETAPIList;

    public RequestHandler(String path, Map<String, APIContext> POSTAPIList, Map<String, APIContext> GETAPIList) {
        this.path = path;
        this.POSTAPIList = POSTAPIList;
        this.GETAPIList = GETAPIList;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private void return404(HttpExchange exchange, String uri) throws IOException {
        Log.RequestLog(uri, "GET", 404);
        String response = "404 (Not Found)";
        exchange.sendResponseHeaders(404, response.length());
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes());
        output.close();
    }

    public void handle(HttpExchange exchange) throws IOException {
        // Get the request URI and file path
        String uri = exchange.getRequestURI().getPath();
        String uri_origin = exchange.getRequestURI().toString();
        String params_get = exchange.getRequestURI().getQuery();


        if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
            // Check if the requesting method is POST then we try to find the handler.
            if (POSTAPIList.get(uri) == null) {
                return404(exchange, uri);
                return;
            }
            Context context;
            try {
                context = new Context(exchange);
            } catch (Exception e) {
                Log.Warn(String.format("Failed to read the params (%s)", uri_origin));
                return;
            }
            POSTAPIList.get(uri).getHandler().handle(context);
            return;
        }
        if (GETAPIList.get(uri) != null) {
            Log.RequestLog(uri, "GET", 200);
            Context context;
            try {
                 context = new Context(exchange);

            } catch (Exception e) {
                Log.Warn(String.format("Failed to read the params (%s)", uri_origin));
                return;
            }
            GETAPIList.get(uri).getHandler().handle(context);
            return;
        }

        if (uri.equals("/")) {
            uri = "/index.html"; // serve index.html for root path
        }
        String filePath = path + uri;

        // Check if the requested file is a directory
        File file = new File(filePath);
        if (file.isDirectory()) {
            // If the requested file is a directory, try to serve the index.html file
            filePath += "/index.html";
            file = new File(filePath);
        }

        // Check if the file exists
        if (!file.exists()) {
            return404(exchange, uri);
        } else {
            // If the file exists, serve the file
            exchange.sendResponseHeaders(200, 0);
            OutputStream output = exchange.getResponseBody();
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }
            input.close();
            output.close();
            Log.RequestLog(uri, "GET", 200);
        }
    }

}
