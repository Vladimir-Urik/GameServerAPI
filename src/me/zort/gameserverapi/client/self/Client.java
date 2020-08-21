package me.zort.gameserverapi.client.self;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {


    public static Client prepareConnection(String ip, String nickname) {
        try {
            String[] spl = ip.split(":");
            return new Client(spl[0], Integer.parseInt(spl[1]), nickname);
        } catch(NumberFormatException e) {
            return null;
        }
    }

    private String address;
    private int port;
    private String nickname;
    private Socket s;
    private InputStream in;
    private OutputStream out;

    private ClientPacketReceiver receiver;

    public Client(String address, int port, String nickname) {
        this.address = address;
        this.port = port;
        this.nickname = nickname;
        this.in = null;
        this.out = null;
    }

    public String openConnection() {
        this.disconnect("Reconnecting.");
        try {
            (this.s = new Socket()).connect(new InetSocketAddress(this.address, this.port));
            this.in = this.s.getInputStream();
            this.out = this.s.getOutputStream();
            (this.receiver = new ClientPacketReceiver(this)).start();
            return null;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public void disconnect(String message) {
        try {
            if(this.s != null && this.s.isConnected()) {
                this.s.close();
                ClientLogger.info("Client disconnected: " + message);
            }
            this.in = null;
            this.out = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return this.nickname;
    }

    public Socket getSocket() {
        return this.s;
    }

    public InputStream getInputStream() {
        return this.in;
    }

    public OutputStream getOutputStream() {
        return this.out;
    }

    public ClientPacketReceiver getReceiver() {
        return this.receiver;
    }

}
