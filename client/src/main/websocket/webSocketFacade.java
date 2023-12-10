package websocket;

import chess.Game;
import com.google.gson.Gson;
import webSocketMessages.userCommands.*;
import java.net.URI;

import javax.websocket.*;

public class webSocketFacade extends Endpoint {
    private String webSocketAddress;
    private String webSocketPort;
    public Session session;
    public Game game;

    public webSocketFacade(String serverAddress, String serverPort){
        try {
            this.webSocketAddress = serverAddress;
            this.webSocketPort = serverPort;
            this.game = new Game();
            URI socketURI = new URI("ws://" + serverAddress + ":" + serverPort + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    System.out.println(message);
                }
            });
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void sendCommand(UserGameCommand command){
        try{
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }catch(java.io.IOException e){
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void onOpen(Session s, EndpointConfig ec){};
    public void end(){
        try {
            this.session.close();
        }catch(java.io.IOException e){
            System.out.println(e.getMessage());
        }
    }
}

