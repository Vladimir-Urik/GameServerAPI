package me.zort.gameserverapi.client;

import me.zort.gameserverapi.event.PlayerJoinEvent;
import me.zort.gameserverapi.server.Server;
import me.zort.gameserverapi.util.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientConnection extends Thread {

    private final Server server;
    private final Socket s;
    private InputStream in;
    private OutputStream out;
    private boolean open;

    private Player player;

    public ClientConnection(Server server, Socket s) {
        this.server = server;
        this.s = s;
        this.open = false;
        this.player = null;
    }

    @Override
    public void run() {
        try {
            this.in = s.getInputStream();
            this.out = s.getOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            this.open = true;
            while ((read = in.read(buffer)) != -1 && this.open && !this.s.isClosed()) {
                String data = new String(buffer, 0, read);
                if (data.contains("nick:") && this.player == null) {
                    this.player = new Player(this, data.split(":")[1]);
                    if (this.server.add(this.player)) {
                        this.server.getAPI().getLogger().info("Player " + this.player.getName() + " joined the game");
                        this.server.getEventManager().callEvent(new PlayerJoinEvent(this.player, this.server));
                    } else {
                        this.server.getAPI().getLogger().info("Player " + this.player.getName() + " dropped: Player with that name is already connected!");
                        this.s.close();
                        break;
                    }
                }
                if (data.contains(":") && this.player != null) {
                    this.server.invokePacket(this, data);
                }
            }
            if (this.player != null) this.server.disconnect(this.player);
            this.open = false;
            this.server.getAPI().getLogger().info("Connection " + s.getInetAddress().getHostAddress() + " disconnected.");
        } catch (IOException e) {
            this.open = false;
            if (this.player != null) this.server.disconnect(this.player);
            this.server.getAPI().getLogger().info("Connection " + s.getInetAddress().getHostAddress() + " dropped: " + e.getMessage());
            try {
                this.s.close();
            } catch (IOException ex) {
            }
        }
    }

    public boolean isOpen() {
        return this.open;
    }

    public void sendPacket(Packet p) {
        if (this.open) {
            try {
                this.out.write(p.getContent().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        this.open = false;
    }

}
