package webSocketHandlers;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.AuthTokenDAO;
import dataAccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import serverModels.ServerAuthToken;
import serverModels.ServerGame;
import services.GameService;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import static chess.ChessGame.TeamColor.*;

@WebSocket
public class webSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        SimpleCommand c = new Gson().fromJson(message, SimpleCommand.class);
        ServerAuthToken auth = new AuthTokenDAO().find(c.getAuthString());
        ServerGame game = new GameDAO().find(c.gameID);
        if (auth == null) {
            ErrorMessage em = new ErrorMessage("Error: bad authentication token");
            session.getRemote().sendString(new Gson().toJson(em));
        } else if (game == null) {
            ErrorMessage em = new ErrorMessage("Error: bad game ID");
            session.getRemote().sendString(new Gson().toJson(em));
        } else {
            LoadMessage lm;
            NotifyMessage nm;
            switch (c.getCommandType()) {
                case JOIN_PLAYER:
                    connections.add(auth.getUsername(), session);
                    JoinPCommand jpm = new Gson().fromJson(message, JoinPCommand.class);
                    if (jpm.playerColor != null && jpm.playerColor == WHITE) {
                        //If the playerColor is white
                        if (game.getWhiteUsername() != null && !game.getWhiteUsername().equals(auth.getUsername())) {
                            //If the white color is already taken
                            ErrorMessage em = new ErrorMessage("Error: Team is taken by a different user");
                            session.getRemote().sendString(new Gson().toJson(em));
                            return;
                        } else if (game.getWhiteUsername()==null){
                            //If the black color is not taken
                            ErrorMessage em = new ErrorMessage("Error: Team is not registered with any user");
                            session.getRemote().sendString(new Gson().toJson(em));
                            return;
                        }
                    }else if(jpm.playerColor != null && jpm.playerColor == BLACK){
                        //If the playerColor is black
                        if (game.getBlackUsername()!=null && !game.getBlackUsername().equals(auth.getUsername())){
                            //If the black color is already taken
                            ErrorMessage em = new ErrorMessage("Error: Team is taken by a different user");
                            session.getRemote().sendString(new Gson().toJson(em));
                            return;
                        }else if (game.getBlackUsername()==null){
                            //If the black color is not taken
                            ErrorMessage em = new ErrorMessage("Error: Team is not registered with any user");
                            session.getRemote().sendString(new Gson().toJson(em));
                            return;
                        }
                    }else{
                        ErrorMessage em = new ErrorMessage("Error: invalid team given");
                        session.getRemote().sendString(new Gson().toJson(em));
                        return;
                    }
                    lm = new LoadMessage(new GameDAO().find(jpm.gameID).getGame());
                    session.getRemote().sendString(new Gson().toJson(lm));
                    nm = new NotifyMessage(auth.getUsername() + " joined the game as " + jpm.playerColor + ".");
                    connections.broadcast(auth.getUsername(), new Gson().toJson(nm));
                    break;
                case JOIN_OBSERVER:
                    connections.add(auth.getUsername(), session);
                    SimpleCommand jom = new Gson().fromJson(message, SimpleCommand.class);
                    lm = new LoadMessage(new GameDAO().find(jom.gameID).getGame());
                    session.getRemote().sendString(new Gson().toJson(lm));
                    nm = new NotifyMessage(auth.getUsername() + " joined the game as observer.");
                    connections.broadcast(auth.getUsername(), new Gson().toJson(nm));
                    break;
                case MAKE_MOVE:
                    MoveCommand mm = MoveCommand.serializer().fromJson(message, MoveCommand.class);
                    if (game.getGame().active == false){
                        ErrorMessage em = new ErrorMessage("Error: Game is over");
                        session.getRemote().sendString(new Gson().toJson(em));
                        return;
                    }
                    if ((game.getGame().getTeamTurn() == BLACK && !game.getBlackUsername().equals(auth.getUsername())) ||
                            (game.getGame().getTeamTurn() == WHITE && !game.getWhiteUsername().equals(auth.getUsername()))){
                        ErrorMessage em = new ErrorMessage("Error: Not your turn");
                        session.getRemote().sendString(new Gson().toJson(em));
                        return;
                    }
                    try{
                        game.getGame().makeMove(mm.move);
                        if(game.getGame().isInCheckmate(game.getGame().getTeamTurn())){
                            nm = new NotifyMessage(auth.getUsername() + " put the other player in checkmate, winning the game.");
                            game.getGame().active = false;
                        }else if(game.getGame().isInStalemate(game.getGame().getTeamTurn())){
                            nm = new NotifyMessage(auth.getUsername() + " put the other player in stalemate, tying the game.");
                            game.getGame().active = false;
                        }else if(game.getGame().isInCheck(game.getGame().getTeamTurn())){
                            nm = new NotifyMessage(auth.getUsername() + " put the other player in check!");
                        }else{
                            nm = new NotifyMessage(auth.getUsername() + " made a move.");
                        }
                        new GameDAO().update(game);
                        lm = new LoadMessage(new GameDAO().find(mm.gameID).getGame());
                        connections.sendAll(new Gson().toJson(lm));
                        connections.broadcast(auth.getUsername(), new Gson().toJson(nm));
                    }catch(InvalidMoveException e){
                        ErrorMessage em = new ErrorMessage("Error: Bad move: "+ e.getMessage());
                        session.getRemote().sendString(new Gson().toJson(em));
                        return;
                    }
                    break;
                case RESIGN:
                    SimpleCommand rm = new Gson().fromJson(message, SimpleCommand.class);
                    if (!game.getWhiteUsername().equals(auth.getUsername()) && !game.getBlackUsername().equals(auth.getUsername())){
                        ErrorMessage em = new ErrorMessage("Error: Non-players cannot resign");
                        session.getRemote().sendString(new Gson().toJson(em));
                        return;
                    }
                    if (game.getGame().active == false){
                        ErrorMessage em = new ErrorMessage("Error: Game is over");
                        session.getRemote().sendString(new Gson().toJson(em));
                        return;
                    }
                    game.getGame().active = false;
                    new GameDAO().update(game);
                    nm = new NotifyMessage(auth.getUsername() + " resigned and left the game.");
                    connections.sendAll(new Gson().toJson(nm));
                    break;
                case LEAVE:
                    SimpleCommand vm = new Gson().fromJson(message, SimpleCommand.class);
                    if(game.getWhiteUsername().equals(auth.getUsername())){
                        game.setWhiteUsername(null);
                    }else if(game.getBlackUsername().equals(auth.getUsername())){
                        game.setBlackUsername(null);
                    }
                    new GameDAO().update(game);
                    nm = new NotifyMessage(auth.getUsername() + " left the game.");
                    connections.broadcast(auth.getUsername(), new Gson().toJson(nm));
                    connections.remove(auth.getUsername());
                    break;
                default:
                    ErrorMessage em = new ErrorMessage("Error: invalid message type");
                    session.getRemote().sendString(new Gson().toJson(em));
            }
        }
        System.out.printf("Recieved: %s", message);
        session.getRemote().sendString("WebSocket response:: " + message);
    }
}
