package xyz.Blockers.Utils.RestfulAPIServer;

public class APIContext {
    private String method;
    private APIHandler handler;

    public APIContext(String method, APIHandler handler) {
        this.method = method;
        this.handler = handler;
    }

    public APIHandler getHandler() {
        return handler;
    }

    public String getMethod() {
        return method;
    }
}
