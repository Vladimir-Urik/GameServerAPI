package me.zort.gameserverapi.server;

import me.zort.gameserverapi.GameServerAPI;
import me.zort.gameserverapi.client.ClientChatPacket;
import me.zort.gameserverapi.client.ClientConnection;
import me.zort.gameserverapi.client.Packet;
import me.zort.gameserverapi.event.EventManager;
import me.zort.gameserverapi.event.PlayerLoginEvent;
import me.zort.gameserverapi.event.PlayerQuitEvent;
import me.zort.gameserverapi.util.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private Server instance;
    private String game;
    private GameServerAPI api;
    private int port;
    private ServerSocket server;
    private EventManager event;
    private List<Player> players;
    private List<ServerPacketAdapter> packets;

    private boolean running;

    public Server(String game, GameServerAPI api, int port) {
        this.init(game, api, port);
    }

    public boolean start() {
        if (!this.running) {
            this.running = true;
            new Thread(() -> {
                api.getLogger().info("Running " + game + " server at port " + port);
                while (running) {
                    try {
                        Socket s = server.accept();
                        getAPI().getLogger().info("Incoming connection (" + s.getInetAddress().getHostAddress() + "), waiting for verification...");
                        PlayerLoginEvent loginEvent = new PlayerLoginEvent(s, instance);
                        getEventManager().callEvent(loginEvent);
                        if (loginEvent.isCancelled()) {
                            s.close();
                            continue;
                        }
                        new ClientConnection(instance, s).start();
                    } catch (IOException e) {
                        api.getLogger().error("Logging connection failure: " + e.getMessage());
                    }
                }
                getAPI().getLogger().info(game + " server at port " + port + " closed");
            }).start();
            return true;
        }
        return false;
    }

    public void stop() {
        if (this.running) this.running = false;
    }

    public EventManager getEventManager() {
        return this.event;
    }

    public boolean add(Player hrac) {
        for (Player p : this.players) {
            if (hrac.getName().equals(p.getName())) {
                return false;
            }
        }
        this.players.add(hrac);
        return true;
    }

    public void disconnect(Player hrac) {
        Player toRemove = null;
        for (Player p : this.players) {
            if (hrac.getName().equals(p.getName())) {
                toRemove = p;
            }
        }
        if (toRemove != null) {
            this.getEventManager().callEvent(new PlayerQuitEvent(hrac, this));
            toRemove.getHandle().close();
            this.players.remove(toRemove);
            this.getAPI().getLogger().info("Player " + hrac.getName() + " left the game");
        }
    }

    public void broadcastMessage(String message) {
        this.sendGlobalPacket(new ClientChatPacket(message));
    }

    public void sendGlobalPacket(Packet p) {
        for (Player player : this.getOnlinePlayers()) {
            player.getHandle().sendPacket(p);
        }
    }

    public void registerPacketListener(ServerPacketAdapter adapter) {
        this.packets.add(adapter);
    }

    public void invokePacket(ClientConnection connection, String input) {
        for (ServerPacketAdapter adapter : this.packets) {
            adapter.invoke(adapter.parse(connection, this, input));
        }
    }

    public List<Player> getOnlinePlayers() {
        return this.players;
    }

    public int getPort() {
        return this.port;
    }

    public GameServerAPI getAPI() {
        return this.api;
    }

    public String getGame() {
        return this.game;
    }

    private void init(String game, GameServerAPI api, int port) {
        this.instance = this;
        this.game = game;
        this.api = api;
        this.port = port;
        try {
            this.server = new ServerSocket(this.port);
            this.event = new EventManager(this);
        } catch (IOException e) {
            this.api.getLogger().error("Cannot create server at port " + this.port, "Error message: " + e.getMessage());
            this.running = false;
            return;
        }
        this.players = new ArrayList<Player>();
        this.packets = new ArrayList<ServerPacketAdapter>();
        this.running = false;
    }

}
