package me.zort.gameserverapi.util;

import me.zort.gameserverapi.client.ClientChatPacket;
import me.zort.gameserverapi.client.ClientConnection;

public class Player {

    private final ClientConnection handle;
    private final String nick;

    public Player(ClientConnection handle, String nick) {
        this.handle = handle;
        this.nick = nick;
    }

    public void sendMessage(String message) {
        this.handle.sendPacket(new ClientChatPacket(message));
    }

    public String getName() {
        return this.nick;
    }

    public boolean isOnline() {
        return this.handle.isOpen();
    }

    public ClientConnection getHandle() {
        return this.handle;
    }

}
