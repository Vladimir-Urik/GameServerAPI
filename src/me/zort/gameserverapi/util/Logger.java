package me.zort.gameserverapi.util;

import me.zort.gameserverapi.GameServerAPI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private final GameServerAPI api;

    public Logger(GameServerAPI api) {
        this.api = api;
    }

    public void info(String... content) {
        for (String s : content) {
            this.log("info", s);
        }
    }

    public void error(String... content) {
        for (String s : content) {
            this.log("error", s);
        }
    }

    public void log(String paramName, String s) {
        this.makeLog(paramName, s);
    }

    private void makeLog(String paramName, String... content) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        for (String s : content) {
            System.out.println("[" + dtf.format(now) + " " + paramName.toUpperCase() + "] " + s);
        }
    }

}
