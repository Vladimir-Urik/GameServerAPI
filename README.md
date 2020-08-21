# GameServerAPI
Game Server API by ZorT. Used for creating game servers based on Socket programming.

public static void main(String[] args) {
        GameServerAPI api = new GameServerAPI();
        Server server = api.createServer(new Server("Test", api, 25565));
        server.registerPacketListener(new ServerPacketAdapter(server) {
            @Override
            public void invoke(PacketResult result) {
                if(result.getPrefix().equalsIgnoreCase("disconnect")) {
                    Player toDisc = null;
                    for(Player p : server.getOnlinePlayers()) {
                        if(p.getName().equalsIgnoreCase(result.getData())) {
                            toDisc = p;
                            break;
                        }
                    }
                    if(toDisc != null) {
                        server.disconnect(toDisc);
                    }
                }
            }
        });
        server.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Client c1 = Client.prepareConnection("127.0.0.1:25565", "Hrac1");
        String drop = null;
        if((drop = c1.openConnection()) != null) {
            ClientLogger.error("Connection closed: " + drop);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.broadcastMessage("cs");
        c1.getReceiver().sendData("disconnect", c1.getNickname());
    }
