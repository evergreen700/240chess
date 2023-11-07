package dataAccess;
import serverModels.*;


/**
 * Data Access Object used to manipulate and access user records within the database.
 */
public class UserDAO {

    /**
     * Adds new user to the database.
     * @param newUser The ServerUser object to store in the database
     * @throws DataAccessException The username or email are already in use on a different account.
     */
    public void insert(ServerUser newUser) throws DataAccessException {
        ServerUser existingUser = find(newUser.getUsername());
        if(existingUser!=null){
            throw new DataAccessException("Username already in use");
        }else {
            var conn = new Database().getConnection();
            try (var preparedStatement = conn.prepareStatement("INSERT INTO chess.users(Username, Password, Email) VALUES(?,?,?)")) {
                preparedStatement.setString(1,newUser.getUsername());
                preparedStatement.setString(2,newUser.getPassword());
                preparedStatement.setString(3,newUser.getEmail());
                preparedStatement.execute();
            } catch (java.sql.SQLException ex) {
                throw new DataAccessException(ex.toString());
            } finally {
                new Database().returnConnection(conn);
            }
        }
    }

    /**
     * Finds the user with the corresponding username in the database. If the name is not currently in use returns null.
     * @param username The username associated with the user account
     * @return Returns a new ServerUser object
     * @throws DataAccessException Could not connect to mySQL server
     */
    public ServerUser find(String username) throws DataAccessException{
        var conn = new Database().getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT * FROM chess.users WHERE Username = ?;")){
            preparedStatement.setString(1,username);
             var results = preparedStatement.executeQuery();
            if (results.next()){
                String upword = results.getString(2);
                String uemail = results.getString(3);
                new Database().returnConnection(conn);
                return new ServerUser(username, upword, uemail);
            }else{
                new Database().returnConnection(conn);
                return null;
            }
        } catch (java.sql.SQLException ex) {
            new Database().returnConnection(conn);
            throw new DataAccessException(ex.toString());
        } finally {
            new Database().returnConnection(conn);
        }
    }

    /**
     * Removes all user entries from the database
     * @throws DataAccessException Could not find mySQL server
     */
    public void deleteAll() throws DataAccessException{
        var conn = new Database().getConnection();
        try (var preparedStatement = conn.prepareStatement("DELETE FROM chess.users")) {
            preparedStatement.execute();
        } catch (java.sql.SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            new Database().returnConnection(conn);
        }
    }
}
