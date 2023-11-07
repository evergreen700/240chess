package dataAccess;
import chess.Game;
import com.google.gson.Gson;
import serverModels.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Data Access Object used for manipulating and accessing game records within the database
 */
public class GameDAO {

    /**
     * Adds a new game to the database
     * @param game Game to be added
     * @return The unique of the game in the database
     * @throws DataAccessException The game provided was null
     */
    public int insert(ServerGame game) throws DataAccessException {
        if (game == null){
            throw new DataAccessException("Cannot handle null value");
        }
        var conn = new Database().getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO chess.games(WhiteUsername, BlackUsername, GameName, GameJSON) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1,game.getWhiteUsername());
            preparedStatement.setString(2,game.getBlackUsername());
            preparedStatement.setString(3,game.getGameName());
            preparedStatement.setString(4,new Gson().toJson(game.getGame()));
            if(preparedStatement.executeUpdate() == 1) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    generatedKeys.next();
                    int id = generatedKeys.getInt(1); // ID of the inserted book
                    return id;
                }
            }else{
                throw new DataAccessException("Execution of statement failed");
            }
        } catch (java.sql.SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            new Database().returnConnection(conn);
        }
    }

    /**
     * Replaces a game already in the database
     * @param game Game to be updated
     * @throws DataAccessException The ID of the game provided is not in the database
     */
    public void update(ServerGame game) throws DataAccessException {
        if (find(game.getGameID()) == null){
            throw new DataAccessException("Trying to update record that does not exist");
        }
        var conn = new Database().getConnection();
        try (var preparedStatement = conn.prepareStatement("UPDATE chess.games SET WhiteUsername = ?, BlackUsername = ?, GameName = ?, GameJSON = ? WHERE GameID = ?")) {
            preparedStatement.setInt(5,game.getGameID());
            preparedStatement.setString(1,game.getWhiteUsername());
            preparedStatement.setString(2,game.getBlackUsername());
            preparedStatement.setString(3,game.getGameName());
            preparedStatement.setString(4,"{}");
            preparedStatement.execute();
        } catch (java.sql.SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            new Database().returnConnection(conn);
        }
    }

    /**
     * Retrieves a game already in the database using its gameID
     * @param gameID Numerical ID that represents the game
     * @return The ServerGame object corresponding with the gameID, or null if the gameID is not in use
     * @throws DataAccessException Server-side error
     */
    public ServerGame find(int gameID) throws DataAccessException{
        var conn = new Database().getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT * FROM chess.games WHERE GameID = ?")){
            preparedStatement.setInt(1, gameID);
            var results = preparedStatement.executeQuery();
            if(results.next()){
                String whiteName = results.getString(2);
                String blackName = results.getString(3);
                String gameName = results.getString(4);
                Game gameObj = Game.serializer().fromJson(results.getString(5), Game.class);
                return new ServerGame(gameID, whiteName, blackName, gameName, gameObj);
            }else{
                return null;
            }
        }catch(java.sql.SQLException ex){
            throw new DataAccessException(ex.toString());
        }finally {
            new Database().returnConnection(conn);
        }
    }

    /**
     * Retrieves all games stored in the database
     * @return An array of ServerGame objects representing all games in the database
     * @throws DataAccessException Server-side error
     */
    public ServerGame[] findAll() throws DataAccessException{
        var conn = new Database().getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT * FROM chess.games")){
            var results = preparedStatement.executeQuery();
            ArrayList<ServerGame> gameList = new ArrayList<>();
            while(results.next()) {
                int gameID = results.getInt(1);
                String whiteName = results.getString(2);
                String blackName = results.getString(3);
                String gameName = results.getString(4);
                Game gameObj = Game.serializer().fromJson(results.getString(5), Game.class);
                gameList.add(new ServerGame(gameID, whiteName, blackName, gameName, gameObj));
            }
            return Arrays.copyOf(gameList.toArray(), gameList.size(), ServerGame[].class);
        }catch(java.sql.SQLException ex){
            throw new DataAccessException(ex.toString());
        } finally {
            new Database().returnConnection(conn);
        }
    }
    /**
     * Removes all game entries from the database
     * @throws DataAccessException Could not access server
     */
    public void deleteAll() throws DataAccessException{
        var conn = new Database().getConnection();
        try (var preparedStatement = conn.prepareStatement("DELETE FROM chess.games")) {
            preparedStatement.execute();
        } catch (java.sql.SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            new Database().returnConnection(conn);
        }
    }
}
