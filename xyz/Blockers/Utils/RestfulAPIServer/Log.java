package xyz.Blockers.Utils.RestfulAPIServer;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // ANSI escape codes for text colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void Debug(String text) {
        printLog("DEBUG", ANSI_GREEN, text);
    }

    public static void Warn(String text) {
        printLog("WARN", ANSI_YELLOW, text);
    }

    public static void RequestLog(String uri, String method, int code) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(formatter);
        if(code == 404 || code == 503 || code == 502 || code == 403) System.out.println(String.format("[RestfulAPIServer] %s %s%d%s %s  %s", method, ANSI_RED, code, ANSI_RESET, timestamp, uri));
        else System.out.println(String.format("[RestfulAPIServer] %s %s%d%s %s  %s", method, ANSI_GREEN, code, ANSI_RESET, timestamp, uri));
    }

    public static void Error(String text) {
        printLog("ERROR", ANSI_RED, text);
    }

    private static void printLog(String level, String color, String text) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(formatter);
        System.out.println("[RestfulAPIServer] " + color + level + ANSI_RESET + String.format(" (%s) : %s", timestamp, text));
    }
}
