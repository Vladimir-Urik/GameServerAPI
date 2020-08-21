package me.zort.gameserverapi.client.self;

import me.zort.gameserverapi.GameServerAPI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientLogger {

    public static void info(String... content) {
        for (String s : content) {
            log("info", s);
        }
    }

    public static void error(String... content) {
        for (String s : content) {
            log("error", s);
        }
    }

    public static void log(String paramName, String s) {
        makeLog(paramName, s);
    }

    private static void makeLog(String paramName, String... content) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        for (String s : content) {
            System.out.println("[" + dtf.format(now) + " " + paramName.toUpperCase() + "] " + s);
        }
    }

}
