import BusinessLayer.Users.User;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/notify")
public class NotificationHandler {
    Collection<Session> sessions = new ArrayList<>();
    @OnOpen
    public void openConnection(Session session){
        sessions.add(session);
    }

    @OnMessage
    public void notified(Session session){
        for(Session s : sessions){
            if(s != session){
                sendMessage(s);
            }
        }
    }

    @OnClose
    public void closeConnection(Session session, CloseReason reason){
        sessions.remove(session);
        System.out.println("Closed server: " + reason.getReasonPhrase());
    }

    @OnError
    public void handleError(Throwable t){
        System.out.println(t.getMessage());
    }

    public void sendMessage(Session s){

    }
}
