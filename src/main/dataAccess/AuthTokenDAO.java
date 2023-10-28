package dataAccess;
import serverModels.*;
import java.util.HashMap;
import java.sql.*;

/**
 * Data Access Object for manipulating and accessing AuthToken records
 */
public class AuthTokenDAO {

    private static HashMap<String, ServerAuthToken> authTokenStorage = new HashMap<String, ServerAuthToken>();

    /**
     * Adds a new AuthToken to the database.
     * @param token The AuthToken being added
     * @throws DataAccessException The AuthToken is already in the database
     */
    public void insert(ServerAuthToken token) throws DataAccessException{
        if (find(token.getAuthToken())!=null){
            throw new DataAccessException("Authentication is not unique");
        }else{
            var conn = new Database().getConnection();
            try (var preparedStatement = conn.prepareStatement("INSERT INTO chess.auth(AuthToken, Username) VALUES(?,?)")) {
                preparedStatement.setString(1,token.getAuthToken());
                preparedStatement.setString(2,token.getUsername());
                preparedStatement.execute();
            } catch (java.sql.SQLException ex) {
                throw new DataAccessException(ex.toString());
            } finally {
                new Database().returnConnection(conn);
            }
        }
    }

    /**
     * Deletes an AuthToken from the database.
     * @param token The AuthToken being deleted
     * @throws DataAccessException AuthToken not found in the database
     */
    public void delete(String token) throws DataAccessException{
        if (find(token) == null){
            throw new DataAccessException("Authentication token not found");
        }else{
            var conn = new Database().getConnection();
            try (var preparedStatement = conn.prepareStatement("DELETE FROM chess.auth WHERE AuthToken = ?")) {
                preparedStatement.setString(1,token);
                preparedStatement.execute();
            } catch (java.sql.SQLException ex) {
                throw new DataAccessException(ex.toString());
            } finally {
                new Database().returnConnection(conn);
            }
        }
    }

    /**
     * Searches the database for a given AuthToken to validate user actions (create game, join game, list games, etc)
     * @param token The AuthToken being checked
     * @return The AuthToken object if it exists, null if not.
     */
    public ServerAuthToken find(String token) throws DataAccessException{
        var conn = new Database().getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT * FROM chess.auth WHERE AuthToken = ?")){
            preparedStatement.setString(1, token);
            var results = preparedStatement.executeQuery();
            if(results.next()){
                String uname = results.getString(2);
                return new ServerAuthToken(uname, token);
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
     * Removes all authToken entries from the database
     */
    public void deleteAll() throws DataAccessException{
        var conn = new Database().getConnection();
        try (var preparedStatement = conn.prepareStatement("DELETE FROM chess.auth")) {
            preparedStatement.execute();
        } catch (java.sql.SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            new Database().returnConnection(conn);
        }
    }
}
