package reqRes;

import chess.Board;
import chess.Game;
import chess.Piece;
import com.google.gson.*;
import serverModels.ServerGame;

import java.lang.reflect.Type;

/**
 * Basic response object used to respond to the HTTP requests
 */
public class baseResponse {
    /**
     * Status message (describes errors if the request could not be met)
     */
    public String message;
    /**
     * The username returned as part of an authentication token when creating an account or logging in
     */
    public String username;
    /**
     * The authentication string returned as part of an authentication token when creating and account or logging in
     */
    public String authToken;
    /**
     * The game information classes returned when listing all existing games
     */
    public gameInfo[] games;

    /**
     * Creates the base response object. All fields are public and can be accessed directly.
     */
    public baseResponse(){};

    /**
     * Inner class used to represent the games when accessing the game list. All elements can be accessed directly.
     */
    public static class gameInfo{

        /**
         * The numerical game ID
         */
        public int gameID;
        /**
         * White player's username
         */
        public String whiteUsername;
        /**
         * Black player's username
         */
        public String blackUsername;
        /**
         * The game's name
         */
        public String gameName;
        public gameInfo(int gameID, String whiteUsername, String blackUsername, String gameName){
            this.gameID = gameID;
            this.whiteUsername = whiteUsername;
            this.blackUsername = blackUsername;
            this.gameName = gameName;
        }
        public gameInfo(ServerGame sg){
            this.gameID = sg.getGameID();
            this.whiteUsername = sg.getWhiteUsername();
            this.blackUsername = sg.getBlackUsername();
            this.gameName = sg.getGameName();
        }
    }
    public static Gson serializer(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(gameInfo.class, new baseResponse.GameInfoAdapter());
        return gsonBuilder.create();
    }
    public static class GameInfoAdapter implements JsonDeserializer<gameInfo> {
        public baseResponse.gameInfo deserialize(JsonElement el, Type type, JsonDeserializationContext ctx){
            var newGameInfo = new Gson().fromJson(el, gameInfo.class);
            return newGameInfo;
        }
    }
}
