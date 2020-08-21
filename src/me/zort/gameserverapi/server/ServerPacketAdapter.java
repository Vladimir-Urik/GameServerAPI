package me.zort.gameserverapi.server;

import me.zort.gameserverapi.client.ClientConnection;

public class ServerPacketAdapter implements PacketAdapter {

    private final Server server;

    public ServerPacketAdapter(Server server) {
        this.server = server;
    }

    @Override
    public void invoke(PacketResult result) { //user Input

    }

    @Override
    public PacketResult parse(ClientConnection connection, Server server, String input) {
        return PacketResult.parse(connection, server, input);
    }
}
