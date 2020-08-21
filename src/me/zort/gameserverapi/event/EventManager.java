package me.zort.gameserverapi.event;

import me.zort.gameserverapi.server.Server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private final Server server;
    private final List<Listener> listeners;

    public EventManager(Server server) {
        this.server = server;
        this.listeners = new ArrayList<Listener>();
    }

    public boolean callEvent(Event event) {
        boolean res = false;
        for (Listener l : this.listeners) {
            for (Method m : l.getClass().getDeclaredMethods()) {
                boolean isEvent = false;
                for (Class<?> param : m.getParameterTypes()) {
                    if (param.equals(event.getClass())) {
                        isEvent = true;
                    }
                }
                if (isEvent) {
                    try {
                        m.invoke(l, event);
                        res = true;
                    } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
                        this.server.getAPI().getLogger().error("Could not call event " + event.getClass(), "Error message: " + e.getMessage());
                    }
                }
            }
        }
        return res;
    }

    public void registerEvents(Listener listener) {
        this.listeners.add(listener);
    }

    public Server getServer() {
        return this.server;
    }

}
