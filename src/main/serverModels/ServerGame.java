package serverModels;
import chess.*;

/**
 * Server model object for a chess game. Contains a @chess.Game object, two users, a game ID number and a game name.
 */
public class ServerGame {
    /**
     * Unique identification code used to reference the game
     */
    private int gameID;

    /**
     * White team username
     */
    private String whiteUsername;

    /**
     * Black team username
     */
    private String blackUsername;

    /**
     * Name displayed for players, more human-friendly identification, etc
     */
    private String gameName;

    /**
     * chess.Game game instance used tp store gameboard and move mechanics
     */
    private chess.Game game;

    /**
     * Generates a new ServerGame object with a given gameID.
     */
    public ServerGame(){
        this.gameID = 0;
        game = new chess.Game();
    }
    public ServerGame(int gameID, String whiteUsername, String blackUsername, String gameName, Game game){
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID(){
        return gameID;
    }

    public String getWhiteUsername(){
        return whiteUsername;
    }
    public String getBlackUsername(){
        return blackUsername;
    }

    public String getGameName(){
        return gameName;
    }
    public chess.Game getGame(){
        return game;
    }

    public void setGameID(int gameID){this.gameID = gameID;}

    public void setWhiteUsername(String white){
        this.whiteUsername = white;
    }
    public void setBlackUsername(String black){
        this.blackUsername = black;
    }
    public void setName(String name){
        this.gameName = name;
    }

}
