package dataAccess;
import serverModels.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Data Access Object used for manipulating and accessing game records within the database
 */
public class GameDAO {
    /**
     * Local storage for ServerGame objects.
     */
    private static HashMap<Integer,ServerGame> gameStorage = new HashMap<Integer, ServerGame>();

    /**
     * Adds a new game to the database
     * @param game Game to be added
     * @throws DataAccessException The ID of the game provided is not unique
     */
    public void insert(ServerGame game) throws DataAccessException {
        if (gameStorage.containsKey(game.getGameID())){
            throw new DataAccessException("GameID already in use");
        }else {
            gameStorage.put(game.getGameID(), game);
        }
    };

    /**
     * Replaces a game already in the database
     * @param game Game to be updated
     * @throws DataAccessException The ID of the game provided is not in the database
     */
    public void update(ServerGame game) throws DataAccessException {
        if (!gameStorage.containsKey(game.getGameID())){
            throw new DataAccessException("No matching gameID to update");
        }else {
            gameStorage.put(game.getGameID(), game);
        }
    }

    /**
     * Retrieves a game already in the database using its gameID
     * @param gameID Numerical ID that represents the game
     * @return The ServerGame object corresponding with the gameID, or null if the gameID is not in use
     */
    public ServerGame find(int gameID){

        return gameStorage.get(gameID);
    }

    /**
     * Retrieves all games stored in the database
     * @return An array of ServerGame objects representing all games in the database
     */
    public ServerGame[] findAll(){
        return Arrays.copyOf(gameStorage.values().toArray(),gameStorage.size(), ServerGame[].class);
    }
    /**
     * Removes all game entries from the database
     */
    public void deleteAll(){
        gameStorage.clear();
    };
}
