package me.zort.gameserverapi.event;

import me.zort.gameserverapi.server.Server;
import me.zort.gameserverapi.util.Player;

public class PlayerJoinEvent implements Event, Cancellable {

    private final Player hrac;
    private final Server server;
    private boolean cancelled;

    public PlayerJoinEvent(Player hrac, Server server) {
        this.hrac = hrac;
        this.server = server;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return this.hrac;
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
