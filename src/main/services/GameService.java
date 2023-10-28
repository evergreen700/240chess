package services;
import dataAccess.*;
import reqRes.*;
import serverModels.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Contains the services for creating, joining, and listing all games.
 */
public class GameService {
    /**
     * A counter used to make sure game ids are unique.
     */
    private static int numGames = 1;

    /**
     * Creates a new game with a given name.
     * @param req Request object. Must contain a valid authToken and valid string for gameName.
     * @return Response object. Contains a unique id in the gameID field.
     */
    public baseResponse createGame(baseRequest req){
        baseResponse res = new baseResponse();
        ServerGame newGame = new ServerGame(numGames);
        if(req.gameName != null){
            newGame.setName(req.gameName);
        }
        ServerAuthToken token = null;
        try{
            token = new AuthTokenDAO().find(req.authToken);
        }catch(DataAccessException ex){
            res.message = "Error: description";
            return res;
        }
        if(token==null){
            res.message = "Error: unauthorized";
            return res;
        }
        try{
            new GameDAO().insert(newGame);
        }catch(DataAccessException exception){
            res.message = "Error: bad request";
            return res;
        }finally{
            gameResponse resg = new gameResponse();
            resg.gameID = numGames;
            numGames++;
            return resg;
        }
    }

    /**
     * Attempts to join a specified game as black, white or a spectator.
     * @param req Request object. Must contain a valid authToken and gameID. Will attempt to join as black or white if the playerColor contains "BLACK" or "WHITE", if empty joins as a spectator.
     * @return res Response object.
     */
    public baseResponse joinGame(baseRequest req){
        baseResponse res = new baseResponse();
        ServerAuthToken token = null;
        try{
            token = new AuthTokenDAO().find(req.authToken);
        }catch(DataAccessException ex){
            res.message = "Error: description";
            return res;
        }
        if(token==null){
            //If the authToken isn't valid
            res.message = "Error: unauthorized";
            return res;
        }
        ServerGame currentGame = new GameDAO().find(req.gameID);
        if(currentGame == null){
            //If the gameID doesn't match up with a game or wasn't attached at all
            res.message = "Error: bad request";
            return res;
        }else{
            if (req.playerColor != null && req.playerColor.equals("WHITE")) {
                //If the playerColor is white
                if (currentGame.getWhiteUsername() != null && !currentGame.getWhiteUsername().equals(token.getUsername())) {
                    //If the white color is already taken
                    res.message = "Error: already taken";
                } else {
                    //If the white color is not taken
                    currentGame.setWhiteUsername(token.getUsername());
                }
            }else if(req.playerColor != null && req.playerColor.equals("BLACK")){
                //If the playerColor is black
                if (currentGame.getBlackUsername()!=null && !currentGame.getBlackUsername().equals(token.getUsername())){
                    //If the black color is already taken
                    res.message = "Error: already taken";
                }else{
                    //If the black color is not taken
                    currentGame.setBlackUsername(token.getUsername());
                }
            }
            try{
                //Putting the game object back on the database
                new GameDAO().update(currentGame);
                return res;
            }catch(DataAccessException exception){
                //This should never happen unless a connection error occurs partway thru
                res.message = "Error: bad request";
                return res;
            }
        }
    }

    /**
     * Lists all games on the database.
     * @param req Request object. Must contain a valid authToken.
     * @return Response object. Contains a list of all games, stored in gameInfo objects (has gameID, whiteUsername, blackUsername, and gameName).
     */
    public baseResponse listGames(baseRequest req){
        baseResponse res = new baseResponse();
        ServerAuthToken token = null;
        try{
            token = new AuthTokenDAO().find(req.authToken);
        }catch(DataAccessException ex){
            res.message = "Error: description";
            return res;
        }
        if(token==null){
            res.message = "Error: unauthorized";
            return res;
        }
        ServerGame[] allGames = new GameDAO().findAll();
        ArrayList<baseResponse.gameInfo> gameList = new ArrayList<baseResponse.gameInfo>();
        for(ServerGame sg: allGames){
            gameList.add(new baseResponse.gameInfo(sg));
        }
        res.games = Arrays.copyOf(gameList.toArray(),gameList.size(), baseResponse.gameInfo[].class);
        return res;
    }

    /**
     * Resets the counter (used when the database is cleared)
     */
    public void resetGameCount(){
        numGames=1;
    }
}
