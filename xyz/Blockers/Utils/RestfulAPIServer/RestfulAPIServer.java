package xyz.Blockers.Utils.RestfulAPIServer;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


public class RestfulAPIServer {
    private HttpServer server;
    private String path = "/";
    private Map<String, APIContext> POSTAPIList;
    private Map<String, APIContext> GETAPIList;
    private RequestHandler requestHandler = null;

    private int port;

    private boolean running = false;
    private boolean initialized = false;

    private boolean CheckPathConflict(String path_api, String path_web) {
        if (path_api.startsWith(path_web)) return true;
        return false;
    }

    public RestfulAPIServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        this.port = port;
        this.initialized = true;
        this.POSTAPIList = new HashMap<String, APIContext>();
        this.GETAPIList = new HashMap<String, APIContext>();
    }

    public boolean start() {
        if (initialized) {
            this.running = true;
            this.requestHandler = new RequestHandler(this.path, this.POSTAPIList, this.GETAPIList);
            server.createContext("/", requestHandler);
            server.start();
            Log.Debug(String.format("Restful API server listening on port %d...", port));
            return true;
        }
        Log.Error("Server not initialized yet.");
        return false;
    }

    public void stop() {
        server.stop(0);
        Log.Debug("Server stopped");
    }

    public void setPath(String path) {
        Log.Debug(String.format("Webserver base path set to %s", path));
        this.path = path;
        if (running) requestHandler.setPath(path);
    }

    public void Static(String rpath, String path) {

    }

    public boolean GET(String path, APIHandler handler) {
        if (CheckPathConflict(path, this.path)) {
            Log.Error(String.format("Get API path (%s) conflict to static web path (%s)", path, this.path));
            return false;
        }
        if(running) {
            Log.Error("Server already started, please initialize all your APIs before start().");
            return false;
        }
        APIContext apicontext = new APIContext("GET", handler);
        GETAPIList.put(path, apicontext);
        Log.Debug(Log.ANSI_GREEN + "[GET] " + Log.ANSI_RESET + path + " ==> " + Log.ANSI_GREEN + handler.toString()+ Log.ANSI_RESET);
        return true;
    }

    public boolean POST(String path, APIHandler handler) {
        if (CheckPathConflict(path, this.path)) {
            Log.Error(String.format("POST API path (%s) conflict to static web path (%s)", path, this.path));
            return false;
        }
        if(running) {
            Log.Error("Server already started, please initialize all your APIs before start();.");
            return false;
        }
        APIContext apicontext = new APIContext("POST", handler);
        POSTAPIList.put(path, apicontext);
        Log.Debug(Log.ANSI_GREEN + "[POST] " + Log.ANSI_RESET + path + " ==> " + Log.ANSI_GREEN + handler.toString()+ Log.ANSI_RESET);
        return true;
    }

    public boolean isRunning() {
        return running;
    }

}