package websocket;

import chess.ChessGame;
import chess.Game;
import com.google.gson.Gson;
import consoleUI.boardDrawer;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.NotifyMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import java.net.URI;

import javax.websocket.*;

import static chess.ChessGame.TeamColor.WHITE;

public class webSocketFacade extends Endpoint {
    private String webSocketAddress;
    private String webSocketPort;
    public Session session;
    public Game game;

    public webSocketFacade(String serverAddress, String serverPort, ChessGame.TeamColor teamColor){
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
                    ServerMessage sm = new Gson().fromJson(message, ServerMessage.class);
                    switch (sm.getServerMessageType()){
                        case LOAD_GAME:
                            LoadMessage lm = LoadMessage.serializer().fromJson(message, LoadMessage.class);
                            game = lm.game;
                            new boardDrawer().drawBoard(game.gameBoard, teamColor);
                            break;
                        case NOTIFICATION:
                            NotifyMessage nm = new Gson().fromJson(message, NotifyMessage.class);
                            System.out.println(nm.message);
                            break;
                        case ERROR:
                            ErrorMessage em = new Gson().fromJson(message, ErrorMessage.class);
                            System.out.println(em.errorMessage);
                            break;
                    }
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

