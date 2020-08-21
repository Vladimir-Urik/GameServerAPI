package me.zort.gameserverapi.event;

import me.zort.gameserverapi.server.Server;

public interface Event {

    default Server getServer() {
        return null;
    }

}
