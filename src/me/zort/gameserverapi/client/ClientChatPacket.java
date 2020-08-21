package me.zort.gameserverapi.client;

public class ClientChatPacket implements Packet {

    private final String message;

    public ClientChatPacket(String message) {
        this.message = message;
    }

    @Override
    public String getPrefix() {
        return "c:";
    }

    @Override
    public String getContent() {
        return "c:" + this.message;
    }
}
