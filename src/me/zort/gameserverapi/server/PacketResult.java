package me.zort.gameserverapi.server;

import me.zort.gameserverapi.client.ClientConnection;

public class PacketResult {

    private final ClientConnection connection;
    private final Server server;
    private final String prefix;
    private final String data;
    public PacketResult(ClientConnection connection, Server server, String prefix, String data) {
        this.connection = connection;
        this.server = server;
        this.prefix = prefix;
        this.data = data;
    }

    public static PacketResult parse(ClientConnection connection, Server server, int input) {
        byte[] buffer = new byte[1024];
        String data = new String(buffer, 0, input);
        if (!data.contains(":")) return null;
        return new PacketResult(connection, server, data.split(":")[0], data.split(":")[1]);
    }

    public static PacketResult parse(ClientConnection connection, Server server, String input) {
        if (!input.contains(":")) return null;
        return new PacketResult(connection, server, input.split(":")[0], input.split(":")[1]);
    }

    public ClientConnection getConnection() {
        return this.connection;
    }

    public Server getServer() {
        return this.server;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getData() {
        return this.data;
    }

}
