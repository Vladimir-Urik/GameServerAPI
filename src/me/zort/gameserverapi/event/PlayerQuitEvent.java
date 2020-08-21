package me.zort.gameserverapi.event;

import me.zort.gameserverapi.server.Server;
import me.zort.gameserverapi.util.Player;

public class PlayerQuitEvent implements Event {

    private final Player hrac;
    private final Server server;

    public PlayerQuitEvent(Player hrac, Server server) {
        this.hrac = hrac;
        this.server = server;
    }

    public Player getPlayer() {
        return this.hrac;
    }

    public Server getServer() {
        return this.server;
    }

}
