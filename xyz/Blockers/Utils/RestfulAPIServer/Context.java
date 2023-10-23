package xyz.Blockers.Utils.RestfulAPIServer;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {
    private HttpExchange exchange;
    private Map<String, String> params = null;

    public Context(HttpExchange exchange) throws Exception {
        this.params = new HashMap<String,String>();
        this.exchange = exchange;
        if(exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            String query = exchange.getRequestURI().getQuery();
            if(query == null) return;
            for (String param : query.split("&")) {
                String[] parts = param.split("=");
                try {
                    params.put(parts[0], parts[1]);
                }catch (Exception e) {
                    Abort();
                    throw new Exception("Exception Params");
                }
            }
        }
        else {
            try {
                InputStream inputStream = exchange.getRequestBody();

                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);

                String body = new String(bytes);

                for (String param : body.split("&")) {
                    String[] parts = param.split("=");
                    params.put(parts[0], parts[1]);
                }
            } catch (IOException e) {
                Abort();
                throw new Exception("Exception Params");
            }
        }
    }

    public Map<String, String> getCookies(HttpExchange exchange) {
        Map<String, String> cookies = new HashMap<String, String>();
        List<String> cookieHeaders = exchange.getRequestHeaders().get("Cookie");
        if (cookieHeaders != null) {
            for (String cookieHeader : cookieHeaders) {
                List<HttpCookie> httpCookies = HttpCookie.parse(cookieHeader);
                for (HttpCookie httpCookie : httpCookies) {
                    cookies.put(httpCookie.getName(), httpCookie.getValue());
                }
            }
        }
        return cookies;
    }

    public String getUserAgent(HttpExchange exchange) {
        return exchange.getRequestHeaders().getFirst("User-Agent");
    }

    public String getRemoteIP() {
        return exchange.getRemoteAddress().getHostName();
    }

    public int getRemotePort() {
        return exchange.getRemoteAddress().getPort();
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void JSON(String jsontext) {
        try {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            // Write the JSON object to the response body.
            exchange.getResponseBody().write(jsontext.getBytes());
            exchange.getResponseBody().close();
        } catch (IOException e) {
            Log.Error("Error responding to the client.");
            Abort();
        }
        exchange.close();
    }

    public void Text(String text) {
        try {
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, 0);
            // Write the JSON object to the response body.
            exchange.getResponseBody().write(text.getBytes());
            exchange.getResponseBody().close();
        } catch (IOException e) {
            Log.Error("Error responding to the client.");
            Abort();
        }
        exchange.close();
    }

    public void file(String filepath) {
        try {
            Path file = Paths.get(filepath);
            if (Files.exists(file) && Files.isRegularFile(file)) {
                String contentType = Files.probeContentType(file);
                if (contentType != null) {
                    exchange.getResponseHeaders().set("Content-Type", contentType);
                }
                exchange.sendResponseHeaders(200, Files.size(file));
                OutputStream responseBody = exchange.getResponseBody();
                Files.copy(file, responseBody);
                responseBody.close();
            } else {
                exchange.sendResponseHeaders(404, 0); // File not found
            }
        } catch (IOException e) {
            Log.Error("Error responding to the client.");
            Abort();
        } catch (Exception e) {
            Log.Error("Error processing the file.");
            Abort();
        }
        exchange.close();
    }

    public void Forbid() {
        try {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(403, 0);
            // Write the JSON object to the response body.
            exchange.getResponseBody().write("403 Forbidden".getBytes());
            exchange.getResponseBody().close();
        } catch (IOException e) {
        }
        exchange.close();
    }


    public void Abort() {
        try {
            exchange.getRequestBody().close();
            // Set the response code to 503 Internal Server Error.
            exchange.sendResponseHeaders(503, 0);
            exchange.getResponseBody().close();
            Log.RequestLog(exchange.getRequestURI().toString(), exchange.getRequestMethod().toString(), 503);
        } catch (IOException e) {
        }
        // Close the exchange.
        exchange.close();
    }
}
