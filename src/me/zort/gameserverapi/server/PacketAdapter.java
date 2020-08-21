package me.zort.gameserverapi.server;

import me.zort.gameserverapi.client.ClientConnection;

public interface PacketAdapter {

    void invoke(PacketResult result);

    default PacketResult parse(ClientConnection connection, Server server, String input) {
        return null;
    }

}
