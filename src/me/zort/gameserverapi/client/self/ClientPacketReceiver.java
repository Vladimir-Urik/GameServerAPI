package me.zort.gameserverapi.client.self;

import me.zort.gameserverapi.GameServerAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

public class ClientPacketReceiver extends Thread {

    private Client client;

    public ClientPacketReceiver(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        if(this.client.getInputStream() == null || this.client.getOutputStream() == null) {
            ClientLogger.error("Client " + client.getSocket().getInetAddress().getHostAddress() + " cant connect:");
            ClientLogger.error("PacketReceiver is not started.");
            return;
        }
        InputStream in = this.client.getInputStream();
        OutputStream out = this.client.getOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        try {
            out.write(("nick:" + client.getNickname()).getBytes());
        } catch (IOException e) {
            this.client.disconnect("Verification error.");
        }
        try {
            while((read = in.read(buffer)) != -1 && this.client.getSocket().isConnected()) {
                String data = new String(buffer, 0, read);
                if(!this.receive(data)) {
                    ClientLogger.error("Invalid packet: " + data.toUpperCase());
                }
            }
            ClientLogger.info("Connection closed.");
        } catch (IOException e) {
            ClientLogger.error("Client " + client.getSocket().getInetAddress().getHostAddress() + " dropped:");
            ClientLogger.error("PacketReceiver crashed because " + e.getMessage());
        }
    }

    public void sendData(String prefix, String data) {
        if(this.client.getOutputStream() == null) return;
        try {
            this.client.getOutputStream().write((prefix + ":" + data).getBytes());
        } catch (IOException e) {
            return;
        }
    }

    private boolean receive(String input) {
        if(!input.contains(":")) return false;
        switch (input.toLowerCase().split(":")[0]) {
            case "c":
                System.out.println(input.toLowerCase().split(":")[1]);
                return true;
        }
        return false;
    }

}
