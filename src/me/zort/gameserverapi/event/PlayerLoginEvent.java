package me.zort.gameserverapi.event;

import me.zort.gameserverapi.server.Server;

import java.net.Socket;

public class PlayerLoginEvent implements Event, Cancellable {

    private final Socket s;
    private final Server server;
    private boolean cancelled;

    public PlayerLoginEvent(Socket s, Server server) {
        this.s = s;
        this.server = server;
        this.cancelled = false;
    }

    public Socket getConnection() {
        return this.s;
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
