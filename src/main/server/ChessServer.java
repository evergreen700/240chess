package server;
import com.google.gson.Gson;
import dataAccess.AuthTokenDAO;
import dataAccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import serverModels.ServerAuthToken;
import spark.*;
import webSocketHandlers.webSocketHandler;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.NotifyMessage;
import webSocketMessages.userCommands.JoinPCommand;
import webSocketMessages.userCommands.MoveCommand;
import webSocketMessages.userCommands.SimpleCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.Map;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_PLAYER;

/**
 * Chess server object that takes in HTTP requests and passes JavaSpark objects to the handlers
 */
//@WebSocket
public class ChessServer {
    /**
     * Map used to match up error messages to status codes
     */
    final private static Map<String, Integer> messageCodes = Map.of(
            "Error: bad request",  400,
            "Error: unauthorized" ,401,
            "Error: already taken",403,
            "Error: description",  500);

    /**
     * Status code mapping function that turns error messages into status numbers
     * @param message Message string. Can also be null
     * @return The corresponding three-digit code. If message is null, returns 200 (success)
     */
    final public static int getStatusCode(String message){
        if(message != null) {
            return messageCodes.get(message);
        }
        else{
            return 200;
        }
    }

    /**
     * Sets up the parameters for spark and declares endpoints
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Spark.port(8080);
        System.out.println("Listening on port 8080");
        Spark.externalStaticFileLocation("src/web");
        Spark.webSocket("/connect", webSocketHandler.class);
        new ChessServer().createRoutes();
    }

    private void createRoutes() {
        Spark.before((req, res) -> System.out.println("Executing route: " + req.pathInfo()));
        Spark.before((req, res) -> System.out.println("AuthToken: " + req.headers("Authorization")));
        Spark.delete("/db", (req, res) -> new handler.clearDB().clearDB(req, res));
        Spark.post("/user", (req, res) -> new handler.addUser().addUser(req, res));
        Spark.post("/session", (req, res) -> new handler.login().login(req, res));
        Spark.delete("/session", (req, res) -> new handler.logout().logout(req, res));
        Spark.get("/game", (req, res) -> new handler.getGames().getGames(req, res));
        Spark.post("/game", (req, res) -> new handler.addGame().addGame(req, res));
        Spark.put("/game", (req, res) -> new handler.joinGame().joinGame(req, res));
    }
}
