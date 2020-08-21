package me.zort.gameserverapi;

import me.zort.gameserverapi.server.Server;
import me.zort.gameserverapi.util.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class GameServerAPI {

    private Logger logger;
    private List<Server> servers;

    public GameServerAPI() {
        this.init();
    }

    public Server createServer(String game, Class<? extends Server> server, int port) {
        try {
            Server s = server.getConstructor(String.class, GameServerAPI.class, Integer.class).newInstance(game, this, port);
            Object[] obj = servers.toArray();
            boolean contains = false;
            for (int x = 0; x < obj.length; x++) {
                Server serv = (Server) obj[x];
                if (serv.getPort() == s.getPort()) contains = true;
            }
            if (!contains) {
                servers.add(s);
                this.getLogger().info(s.getGame() + " server at port " + port + " created");
                return s;
            } else {
                this.getLogger().error("Server with port " + port + " is already created");
                return null;
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            this.getLogger().error("Could not start server at port " + port + " with named instance " + server.getClass());
            e.printStackTrace();
        }
        return null;
    }

    public Server createServer(Server server) {
        Server s = server;
        Object[] obj = servers.toArray();
        boolean contains = false;
        for (int x = 0; x < obj.length; x++) {
            Server serv = (Server) obj[x];
            if (serv.getPort() == s.getPort()) contains = true;
        }
        if (!contains) {
            servers.add(s);
            this.getLogger().info(server.getGame() + " server at port " + s.getPort() + " created");
            return s;
        } else {
            this.getLogger().error("Server with port " + s.getPort() + " is already saved.");
            return null;
        }
    }

    public boolean deleteServer(Server s) {
        s.stop();
        Server toRemove = null;
        for (Server serv : this.servers) {
            if (serv.getPort() == s.getPort()) {
                toRemove = serv;
            }
        }
        if (toRemove != null) {
            this.servers.remove(toRemove);
            return true;
        } else {
            return false;
        }
    }

    public static List<Class<?>> getAvailablePackets() {
        try {
            List<Class<?>> res = new ArrayList<Class<?>>();
            for (Class c : getClasses("me.zort.gameserverapi.client")) {
                if (c.getInterfaces().length > 0) {
                    for (Class interf : c.getInterfaces()) {
                        if (interf.getName().toLowerCase().contains("packet")) {
                            res.add(c);
                        }
                    }
                }
            }
            return res;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Logger getLogger() {
        return this.logger;
    }

    private void init() {
        this.logger = new Logger(this);
        this.servers = new ArrayList<Server>();
    }

    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}
