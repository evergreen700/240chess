package webSocketHandlers;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session) {
        var connection = new Connection(visitorName, session);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String senderName, String notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(senderName)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

    public void sendAll(String notification) throws IOException{
        var removeList = new ArrayList<Connection>();
        for (var c: connections.values()){
            if (c.session.isOpen()){
                c.send(notification.toString());
            }else {
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}
